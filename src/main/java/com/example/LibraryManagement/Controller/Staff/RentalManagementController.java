package com.example.LibraryManagement.Controller.Staff;

import com.example.LibraryManagement.Model.*;
import com.example.LibraryManagement.Repository.*;
import com.example.LibraryManagement.Request.*;
import com.example.LibraryManagement.Service.BorrowIndexService;
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

    @GetMapping("rentals")
    public String getAllBorrowIndex(Model model) {
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
                    book.setFirstImageName("nullI.png");
                }
            }
        });
        model.addAttribute("borrowIndexes", borrowIndexList);
        return "Staff/rentals";
    }
    @GetMapping("add-rental")
    public String addRental(Model model) {
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
        List<Book> bookList = borrowIndexService.getAllBooks();
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

            // Thông báo thành công
            redirectAttributes.addFlashAttribute("success", "Rental created successfully!");
        } else {
            // Thông báo lỗi nếu có dữ liệu không hợp lệ
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
                               RedirectAttributes redirectAttributes) {
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
    public String completeRental(@PathVariable("id") Integer borrowIndexId, Model model){
        List<Student> studentList = borrowIndexService.getAllStudents();
        BorrowIndex borrowIndex = borrowIndexService.getBorrowIndexById(borrowIndexId);
        List<BookCondition> bookConditionList =  borrowIndexService.getAllBookConditionsComplete(borrowIndex.getConditionBefore().getBookConditionId());
        List<Book> bookList = borrowIndexService.getAllBooks();
        model.addAttribute("studentList", studentList);
        model.addAttribute("bookConditionList", bookConditionList);
        model.addAttribute("bookList", bookList);
        model.addAttribute("borrowIndex", borrowIndex);
        return "Staff/complete-rental";
    }
    @PostMapping("/complete-rental/{id}")
    public String completeRental(@PathVariable("id") Integer borrowIndexId,
                               @ModelAttribute CompleteRentalRequest completeRentalRequest,
                               RedirectAttributes redirectAttributes) {
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
                late = "Return late: " +valueLate;
            }
            if (conditionBefore != null && conditionAfter != null) {
                if (conditionAfter.getBookConditionId() != 6){
                    int conditionDifference = conditionAfter.getBookConditionId() - conditionBefore.getBookConditionId() ;
                    if (conditionDifference > 0) {
                        value += conditionDifference * 15000; // 15000 cho mỗi mức giảm
                        valueDam += conditionDifference * 15000;;
                        dam = "Level of damage the book: " + valueDam;
                    }
                }
                if (conditionAfter.getBookConditionId() == 6) {
                    float price = book.getPrice();
                    switch (conditionBefore.getBookConditionId()) {
                        case 1: value += price; value += price; break;
                        case 2: value += price * 85 / 100; valueLost += price * 85 / 100; break;
                        case 3: value += price * 70 / 100; valueLost += price * 70 / 100; break;
                        case 4: value += price * 55 / 100; valueLost += price * 55 / 100; break;
                        case 5: value += price * 40 / 100; valueLost += price * 40 / 100; break;
                        default: break;
                    }
                    lost = "Lost book: " + valueLost;
                }
                if (value > 0) {
                    Optional<BorrowFine> existingFineOpt = Optional.ofNullable(borrowFineRepository.getBorrowFineByBorrowIndex(existingBorrowIndex));

                    BorrowFine borrowFine;
                    if (existingFineOpt.isPresent()) {
                        borrowFine = existingFineOpt.get();
                        borrowFine.setValue((int) value);
                        borrowFine.setReason(late+""+dam+""+lost);
                        borrowFine.setStatus("Unpaid");
                    } else {
                        borrowFine = new BorrowFine();
                        borrowFine.setBorrowIndex(existingBorrowIndex);
                        borrowFine.setReason(late+""+dam+""+lost);
                        borrowFine.setStatus("Unpaid");
                        borrowFine.setValue((int) value);
                    }

                    borrowFineRepository.save(borrowFine);
                    redirectAttributes.addFlashAttribute("success", "Fine created/updated successfully! Please check the information.");
                    return "redirect:/staff/check-fine/" + borrowIndexId;
                }

                redirectAttributes.addFlashAttribute("success", "Rental complete successfully!");
            }
        }else {
            redirectAttributes.addFlashAttribute("error", "Rental not found!");
        }
        return "redirect:/staff/rentals";

    }
    @GetMapping("/check-fine/{id}")
    public String checkFine(@PathVariable("id") Integer borrowIndexId, Model model) {
        Optional<BorrowIndex> borrowIndexOpt = borrowIndexRepository.findById(borrowIndexId);

        if (borrowIndexOpt.isPresent()) {
            BorrowIndex borrowIndex = borrowIndexOpt.get();

            Optional<BorrowFine> borrowFineOpt = Optional.ofNullable(borrowIndexService.getBorrowFineByBorrowIndex(borrowIndex));

            model.addAttribute("borrowIndex", borrowIndex);

            if (borrowFineOpt.isPresent()) {
                BorrowFine borrowFine = borrowFineOpt.get();
                model.addAttribute("borrowFine", borrowFine);
            } else {
                model.addAttribute("borrowFine", null);
            }

            return "Staff/check-fine";
        } else {
            model.addAttribute("error", "Borrow Index not found!");
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
