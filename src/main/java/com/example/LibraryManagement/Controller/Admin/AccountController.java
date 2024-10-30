package com.example.LibraryManagement.Controller.Admin;

import com.example.LibraryManagement.Model.Role;
import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Service.AdminService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AccountController {
    private final AdminService adminService;
    @GetMapping("students")
    public String getAllStudentAccount(Model model) {
        List<Student> students = adminService.getAllStudents();
        model.addAttribute("students", students);
        return "Admin/students";
    }
    @GetMapping("staffs")
    public String getAllStaffAccount(Model model) {
        List<Staff> staffs = adminService.getAllStaffs();
        model.addAttribute("staffs", staffs);
        return "Admin/staffs";
    }
    @GetMapping("/staff/register")
    public String registerStaff(Model model) {
        List<Role> roles = adminService.getAllRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("user", new Staff());
        return "Admin/staff-add";
    }
    @GetMapping("/student/register")
    public String registerStudent(Model model) {
        model.addAttribute("user", new Student());
        return "Admin/staff-add";
    }
    @PostMapping("/student/register")
    public String registerStudent(@Valid @ModelAttribute("student") Student student, RedirectAttributes redirectAttributes, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "Admin/student-add";
        }
        String saveStatus =  adminService.saveStudent(student);
        if ("Email already exists".equals(saveStatus)) {
            redirectAttributes.addFlashAttribute("error", "Email is already used by another account.");
            return "redirect:/admin/student/register";
        }
        return "redirect:/admin/students";
    }
    @PostMapping("/staff/register")
    public String registerStaff(@Valid @ModelAttribute("staff") Staff staff, RedirectAttributes redirectAttributes, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "Admin/staff-add";
        }
        String saveStatus =  adminService.saveStaff(staff);
        if ("Email already exists".equals(saveStatus)) {
            redirectAttributes.addFlashAttribute("error", "Email is already used by another account.");
            return "redirect:/admin/staff/register";
        }
        return "redirect:/admin/staffs";
    }
    @GetMapping("/staff/delete/{id}")
    public String deleteStaff(@PathVariable("id") int staffId, Model model) {
        adminService.deleteStaffById(staffId);
        return "redirect:/admin/staffs"; // Đường dẫn đến trang hiển thị chi tiết
    }
    @GetMapping("/staff/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer staffId, Model model) {
        Staff staff = adminService.findStaffById(staffId);
        List<Role> roles = adminService.getAllRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("staff", staff);
        return "Admin/staff-update";
    }
    @PostMapping("/staff/update/{id}")
    public String updateStaff(@PathVariable("id") Integer staffId,
                              @ModelAttribute("staff") Staff staff,
                              RedirectAttributes redirectAttributes) {
        // Gọi phương thức service để cập nhật thông tin nhân viên
        adminService.updateStaff(staffId, staff);
        redirectAttributes.addFlashAttribute("success", "Staff information updated successfully.");
        return "redirect:/admin/staffs";
    }




}
