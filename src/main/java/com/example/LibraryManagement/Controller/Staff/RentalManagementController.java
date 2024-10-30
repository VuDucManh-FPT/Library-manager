package com.example.LibraryManagement.Controller.Staff;

import com.example.LibraryManagement.Model.*;
import com.example.LibraryManagement.Repository.*;
import com.example.LibraryManagement.Request.NewPasswordRequest;
import com.example.LibraryManagement.Request.NewRentalRequest;
import com.example.LibraryManagement.Request.UpdateRentalRequest;
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

    @GetMapping("rentals")
    public String getAllBorrowIndex(Model model) {
        List<BorrowIndex> borrowIndexList = borrowIndexService.getAllBorrowIndex();
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
        List<BookCondition> bookConditionList = borrowIndexService.getAllBookConditions();
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
        List<BookCondition> bookConditionList = borrowIndexService.getAllBookConditions();
        List<Book> bookList = borrowIndexService.getAllBooks();
        model.addAttribute("studentList", studentList);
        model.addAttribute("bookConditionList", bookConditionList);
        model.addAttribute("bookList", bookList);
        model.addAttribute("borrowIndex", borrowIndex);
        // Thêm các danh sách cần thiết khác (sinh viên, sách, điều kiện...)

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
            existingBorrowIndex.setConditionAfter(bookConditionRepository.findById(updateRentalRequest.getConditionAfterID()).orElse(null)); // Thêm trường conditionAfter
            existingBorrowIndex.setEstimateDate(updateRentalRequest.getEstimateDate());
            existingBorrowIndex.setReturnDate(updateRentalRequest.getReturnDate()); // Nếu có trường này trong yêu cầu

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



}
