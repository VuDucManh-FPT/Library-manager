package com.example.LibraryManagement.Controller.Staff;

import com.example.LibraryManagement.Model.*;
import com.example.LibraryManagement.Repository.*;
import com.example.LibraryManagement.Request.*;
import com.example.LibraryManagement.Response.NavbarResponse;
import com.example.LibraryManagement.Response.VNPayResponse;
import com.example.LibraryManagement.Security.JwtProvider;
import com.example.LibraryManagement.Service.BorrowIndexService;
import com.example.LibraryManagement.Service.PaymentService;
import com.example.LibraryManagement.Service.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/staff")
@PreAuthorize("hasAuthority('STAFF')")
@AllArgsConstructor
public class RentalManagementController {
    private final BorrowIndexService borrowIndexService;
    private final StaffRepository staffRepository;
    private final BookConditionRepository bookConditionRepository;
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;
    private final BorrowIndexRepository borrowIndexRepository;
    private final BorrowFineRepository borrowFineRepository;
    private final PaymentService paymentService;
    private final ServiceImpl serviceImpl;
    private final JwtProvider jwtProvider;

    @GetMapping("rentals")
    public String getAllBorrowIndex(Model model, HttpServletRequest request) {
        List<BorrowIndex> borrowIndexList = borrowIndexService.getAllBorrowIndex();
        borrowIndexList.forEach(borrowIndex -> {
            BorrowFine borrowFine = borrowFineRepository.getBorrowFineByBorrowIndex(borrowIndex);
            if (borrowFine != null) {
                borrowIndex.setFines(List.of(borrowFine));
            }
            Book book = borrowIndex.getBook();
            if (book != null) {
                if (book.getBookImages() != null && !book.getBookImages().isEmpty()) {
                    book.setFirstImageName(book.getBookImages().get(0).getImageURL());
                } else {
                    book.setFirstImageName("03.jpg");
                }
            }
        });
        NavbarResponse navbarData = serviceImpl.getNavbarAdminData(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        model.addAttribute("borrowIndexes", borrowIndexList);
        return "Staff/rentals";
    }
    @GetMapping("add-rental")
    public String addRental(Model model, HttpServletRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = "";
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();  // Lấy email từ UserDetails
        }

        // Tìm thông tin Staff từ database dựa trên email
        Optional<Staff> staffOpt = staffRepository.findByStaffEmail(email);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            model.addAttribute("staffId", staff.getStaffId());
            model.addAttribute("staffName", staff.getStaffName());
        } else {
            // Xử lý trường hợp không tìm thấy staff
            model.addAttribute("error", "Staff not found!");
        }
        List<Student> studentList = borrowIndexService.getAllStudents();
        List<BookCondition> bookConditionList = borrowIndexService.getAllBookConditionsAdd();
        List<Book> bookList = borrowIndexService.getAllBooksActive();
        NavbarResponse navbarData = serviceImpl.getNavbarAdminData(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        model.addAttribute("studentList", studentList);
        model.addAttribute("bookConditionList", bookConditionList);
        model.addAttribute("bookList", bookList);
        return "Staff/add-rental";
    }
    @PostMapping("add-rental")
    public String createRental(@ModelAttribute NewRentalRequest newRentalRequest, RedirectAttributes redirectAttributes) {
        // Tạo đối tượng BorrowIndex mới
        BorrowIndex newBorrow = new BorrowIndex();
        // Lấy các giá trị cần thiết từ NewRentalRequest
        int staffId = newRentalRequest.getStaffID();
        int studentId = newRentalRequest.getStudentID();
        int bookId = newRentalRequest.getBookID();
        int conditionBeforeId = newRentalRequest.getConditionBeforeID();
        Date estimateDate = newRentalRequest.getEstimateDate();
        Date startDate = newRentalRequest.getStartDate();

        // Tìm các bản ghi liên quan trong các bảng Staff, Student, Book, BookCondition
        Optional<Staff> staffOpt = staffRepository.findById(staffId);
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        Optional<BookCondition> conditionBeforeOpt = bookConditionRepository.findById(conditionBeforeId);
        List<BorrowIndex> borrowIndices = borrowIndexRepository.findByStudentAndReturnDateIsNull(studentOpt.get());
        if (borrowIndices.size()>2) {
            redirectAttributes.addFlashAttribute("error", "This student had borrow 3 book! Please return before borrowing new one.");
            return "redirect:/staff/rentals" ;
        }
        Optional<BorrowFine> borrowFine  = Optional.ofNullable(borrowFineRepository.findByBorrowIndexStudentAndStatus(studentOpt, "Unpaid"));
        if(borrowFine.isPresent()){
            redirectAttributes.addFlashAttribute("error", "This student had borrow fine! Please pay before borrowing new one.");
            return "redirect:/staff/check-fine/" + borrowFine.get().getBorrowIndex().getBorrowIndexId();
        }
        if (staffOpt.isPresent() && studentOpt.isPresent() && bookOpt.isPresent() && conditionBeforeOpt.isPresent()) {
            // Thiết lập các thuộc tính cho BorrowIndex
            newBorrow.setStaff(staffOpt.get());
            newBorrow.setStudent(studentOpt.get());
            newBorrow.setBook(bookOpt.get());
            newBorrow.setConditionBefore(conditionBeforeOpt.get());
            newBorrow.setEstimateDate(estimateDate);
            newBorrow.setStartDate(startDate);

            // Lưu vào cơ sở dữ liệu
            borrowIndexRepository.save(newBorrow);
//            borrowIndexService.updateBookAfterCreateBorrowIndex(bookOpt.get());
            // Thông báo thành công
            redirectAttributes.addFlashAttribute("success", "Rental created successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid data. Please try again.");
        }

        return "redirect:/staff/rentals";
    }
    @GetMapping("/update-rental/{id}")
    public String showEditForm(@PathVariable("id") Integer borrowIndexId, Model model) {
        // Lấy thông tin từ dịch vụ dựa trên borrowIndexId
        List<Student> studentList = borrowIndexService.getAllStudents();
        BorrowIndex borrowIndex = borrowIndexService.getBorrowIndexById(borrowIndexId);
        List<BookCondition> bookConditionList = borrowIndexService.getAllBookConditionsAdd();
        List<Book> bookList = borrowIndexService.getAllBooks();
        model.addAttribute("studentList", studentList);
        model.addAttribute("bookConditionList", bookConditionList);
        model.addAttribute("bookList", bookList);
        model.addAttribute("borrowIndex", borrowIndex);
        return "Staff/update-rental";
    }
    @PostMapping("/update-rental/{id}")
    public String updateRental(@PathVariable("id") Integer borrowIndexId,
                               @ModelAttribute UpdateRentalRequest updateRentalRequest,
                               RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String token = jwtProvider.getJwtFromCookies(request);
        String email = jwtProvider.getEmail(token);
        Optional<Staff> updateStaff = staffRepository.findByStaffEmail(email);
        // Lấy thông tin bản ghi BorrowIndex hiện tại dựa trên ID
        Optional<BorrowIndex> existingBorrowIndexOpt = borrowIndexRepository.findById(borrowIndexId);

        if (existingBorrowIndexOpt.isPresent()) {
            BorrowIndex existingBorrowIndex = existingBorrowIndexOpt.get();

            // Cập nhật các thuộc tính của bản ghi BorrowIndex
            existingBorrowIndex.setStaff(staffRepository.findById(updateRentalRequest.getStaffID()).orElse(null));
            existingBorrowIndex.setStudent(studentRepository.findById(updateRentalRequest.getStudentID()).orElse(null));
            existingBorrowIndex.setBook(bookRepository.findById(updateRentalRequest.getBookID()).orElse(null));
            existingBorrowIndex.setConditionBefore(bookConditionRepository.findById(updateRentalRequest.getConditionBeforeID()).orElse(null));
            existingBorrowIndex.setEstimateDate(updateRentalRequest.getEstimateDate());
            existingBorrowIndex.setStartDate(updateRentalRequest.getStartDate()); // Nếu có trường này trong yêu cầu
            existingBorrowIndex.setUpdateStaff(updateStaff.get());
            // Lưu thay đổi vào cơ sở dữ liệu
            borrowIndexRepository.save(existingBorrowIndex);

            // Thông báo thành công
            redirectAttributes.addFlashAttribute("success", "Rental updated successfully!");
        } else {
            // Thông báo lỗi nếu không tìm thấy bản ghi
            redirectAttributes.addFlashAttribute("error", "Rental not found!");
        }

        return "redirect:/staff/rentals"; // Điều hướng lại đến danh sách rentals
    }
    @GetMapping("/delete-rental/{id}")
    public String deleteRental(@PathVariable("id") Integer borrowIndexId, Model model){
        try {
            borrowIndexRepository.deleteById(borrowIndexId);
            model.addAttribute("success", "Rental deleted successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to delete rental: " + e.getMessage());
        }
        return "redirect:/staff/rentals";
    }
    @GetMapping("/complete-rental/{id}")
    public String completeRental(@PathVariable("id") Integer borrowIndexId, Model model, HttpServletRequest request){
        List<Student> studentList = borrowIndexService.getAllStudents();
        BorrowIndex borrowIndex = borrowIndexService.getBorrowIndexById(borrowIndexId);
        List<BookCondition> bookConditionList =  borrowIndexService.getAllBookConditionsComplete(borrowIndex.getConditionBefore().getBookConditionId());
        List<Book> bookList = borrowIndexService.getAllBooks();

        NavbarResponse navbarData = serviceImpl.getNavbarAdminData(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        model.addAttribute("studentList", studentList);
        model.addAttribute("bookConditionList", bookConditionList);
        model.addAttribute("bookList", bookList);
        model.addAttribute("borrowIndex", borrowIndex);
        return "Staff/complete-rental";
    }
    @PostMapping("/complete-rental/{id}")
    public String completeRental(@PathVariable("id") Integer borrowIndexId,
                                 @ModelAttribute CompleteRentalRequest completeRentalRequest,
                                 RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String token = jwtProvider.getJwtFromCookies(request);
        String email = jwtProvider.getEmail(token);
        Optional<Staff> completeStaff = staffRepository.findByStaffEmail(email);
        // Lấy thông tin bản ghi BorrowIndex hiện tại dựa trên ID
        Optional<BorrowIndex> existingBorrowIndexOpt = borrowIndexRepository.findById(borrowIndexId);

        if (existingBorrowIndexOpt.isPresent()) {
            BorrowIndex existingBorrowIndex = existingBorrowIndexOpt.get();

            // Cập nhật các thuộc tính của bản ghi BorrowIndex
            existingBorrowIndex.setStaff(staffRepository.findById(completeRentalRequest.getStaffID()).orElse(null));
            existingBorrowIndex.setStudent(studentRepository.findById(completeRentalRequest.getStudentID()).orElse(null));
            existingBorrowIndex.setBook(bookRepository.findById(completeRentalRequest.getBookID()).orElse(null));
            existingBorrowIndex.setConditionAfter(bookConditionRepository.findById(completeRentalRequest.getConditionAfterID()).orElse(null));
            existingBorrowIndex.setEstimateDate(completeRentalRequest.getEstimateDate());
            existingBorrowIndex.setReturnDate(completeRentalRequest.getReturnDate());
            existingBorrowIndex.setCompleteStaff(completeStaff.get());
            borrowIndexRepository.save(existingBorrowIndex);

            float value = 0;
            float valueDam = 0;
            float valueLost = 0;
            String late ="";
            String dam ="";
            String lost ="";
            BookCondition conditionBefore = existingBorrowIndex.getConditionBefore();
            BookCondition conditionAfter = existingBorrowIndex.getConditionAfter();
            Book book = existingBorrowIndex.getBook();
            LocalDate estimateDate = existingBorrowIndex.getEstimateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate returnDate = completeRentalRequest.getReturnDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (returnDate.isAfter(estimateDate)) {
                long daysLate = ChronoUnit.DAYS.between(estimateDate, returnDate);
                value += daysLate * 5000;
                float valueLate = value;
                late = "Return late: " +valueLate +" |";
            }
            if (conditionBefore != null && conditionAfter != null) {
                if (conditionAfter.getBookConditionId() != 6){
                    int conditionDifference = conditionAfter.getBookConditionId() - conditionBefore.getBookConditionId() ;
                    if (conditionDifference > 0) {
                        value += conditionDifference * 15000; // 15000 cho mỗi mức giảm
                        valueDam += conditionDifference * 15000;;
                        dam = "Level of damage the book: " + valueDam +" |";
                    }
                }
                if (conditionAfter.getBookConditionId() == 6) {
                    switch (conditionBefore.getBookConditionId()) {
                        case 1: value += book.getPrice(); valueLost = value; break;
                        case 2: value += book.getPrice() * 85 / 100; valueLost = value; break;
                        case 3: value += book.getPrice() * 70 / 100; valueLost = value; break;
                        case 4: value += book.getPrice() * 55 / 100; valueLost = value; break;
                        case 5: value += book.getPrice() * 40 / 100; valueLost = value; break;
                        default: break;
                    }
                    lost = "Lost book: " + valueLost+" |";
                }
                if (value > 0) {
                    Optional<BorrowFine> existingFineOpt = Optional.ofNullable(borrowFineRepository.getBorrowFineByBorrowIndex(existingBorrowIndex));

                    BorrowFine borrowFine;
                    if (existingFineOpt.isPresent()) {
                        borrowFine = existingFineOpt.get();
                        borrowFine.setValue((int) value);
                        borrowFine.setReason(late+" "+dam+" "+lost);
                        borrowFine.setStatus("Unpaid");
                    } else {
                        borrowFine = new BorrowFine();
                        borrowFine.setBorrowIndex(existingBorrowIndex);
                        borrowFine.setReason(late+" "+dam+" "+lost);
                        borrowFine.setStatus("Unpaid");
                        borrowFine.setValue((int) value);
                    }

                    borrowFineRepository.save(borrowFine);
                    VNPayResponse vnPayResponse = paymentService.createVnPayPayment(request, (int) value, (long) borrowFine.getBorrowFineId());
                    String paymentUrl = vnPayResponse.getPaymentUrl();
                    redirectAttributes.addFlashAttribute("paymentUrl", paymentUrl);
                    redirectAttributes.addFlashAttribute("success", "Fine created/updated successfully! Please check the information.");
                    return "redirect:/staff/check-fine/" + borrowIndexId;
                }
                BorrowFine borrowFine = borrowFineRepository.getBorrowFineByBorrowIndex(existingBorrowIndex);
                if (borrowFine!=null){
                    borrowFineRepository.delete(borrowFine);
                }
                redirectAttributes.addFlashAttribute("success", "Rental complete successfully!");
            }
        }else {
            redirectAttributes.addFlashAttribute("error", "Rental not found!");
        }
        return "redirect:/staff/rentals";

    }
    @GetMapping("/check-fine/{id}")
    public String checkFine(@PathVariable("id") Integer borrowIndexId, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Optional<BorrowIndex> borrowIndexOpt = borrowIndexRepository.findById(borrowIndexId);
        NavbarResponse navbarData = serviceImpl.getNavbarAdminData(request);

        if (borrowIndexOpt.isPresent()) {
            BorrowIndex borrowIndex = borrowIndexOpt.get();

            Optional<BorrowFine> borrowFineOpt = Optional.ofNullable(borrowIndexService.getBorrowFineByBorrowIndex(borrowIndex));

            model.addAttribute("borrowIndex", borrowIndex);

            if (borrowFineOpt.isPresent()) {
                BorrowFine borrowFine = borrowFineOpt.get();
                VNPayResponse vnPayResponse = paymentService.createVnPayPayment(request, (int) borrowFine.getValue(), (long) borrowFine.getBorrowFineId());
                String paymentUrl = vnPayResponse.getPaymentUrl();
                model.addAttribute("paymentUrl", paymentUrl);
                model.addAttribute("borrowFine", borrowFine);
            } else {
                model.addAttribute("borrowFine", null);
            }
            model.addAttribute("email", navbarData.email);
            model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
            model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
            return "Staff/check-fine";
        } else {
            redirectAttributes.addFlashAttribute("error", "Borrow Index not found!");
            return "redirect:/staff/rentals";
        }
    }
    @PostMapping("/check-fine/{id}")
    public String checkFine(@PathVariable("id") Integer borrowIndexId,
                                  @ModelAttribute FineRequest fineRequest,@RequestParam("status") String status,
                                  RedirectAttributes redirectAttributes) {
        Optional<BorrowIndex> borrowIndexOpt = borrowIndexRepository.findById(borrowIndexId);

        if (borrowIndexOpt.isPresent()) {
            BorrowIndex borrowIndex = borrowIndexOpt.get();
            BorrowFine borrowFine = borrowIndexService.getBorrowFineByBorrowIndex(borrowIndex);
            borrowFine.setBorrowIndex(borrowIndex);
            borrowFine.setReason(fineRequest.getReason());
            borrowFine.setStatus(status.equals("Paid") ? "Paid" : "Unpaid");
            borrowFine.setValue(fineRequest.getValue());
            borrowFineRepository.save(borrowFine);
            if (status.equals("Paid")) {
                redirectAttributes.addFlashAttribute("success", "Fine marked as Paid successfully!");
            } else {
                redirectAttributes.addFlashAttribute("success", "Borrow complete successfully! Please proceed with fine payment.");
            }
            return "redirect:/staff/rentals";
        } else {
            redirectAttributes.addFlashAttribute("error", "Borrow Index not found!");
            return "redirect:/staff/rentals";
        }
    }



}
