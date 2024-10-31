package com.example.LibraryManagement.Controller.Admin;

import com.example.LibraryManagement.Model.BorrowIndex;
import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.BorrowIndexRepository;
import com.example.LibraryManagement.Service.AdminService;
import com.example.LibraryManagement.Service.BorrowIndexService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AccountController {
    private final AdminService adminService;
    @GetMapping("accounts")
    public String getAllBorrowIndex(Model model) {
        List<Student> students = adminService.getAllStudents();
        List<Staff> staffs = adminService.getAllStaffs();
        model.addAttribute("students", students);
        model.addAttribute("staffs", staffs);
        return "Admin/accounts";
    }

}
