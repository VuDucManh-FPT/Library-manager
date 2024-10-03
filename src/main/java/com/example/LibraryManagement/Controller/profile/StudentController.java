package com.example.LibraryManagement.Controller.profile;

import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Service.StudentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentServiceImpl studentServiceImpl;

    @GetMapping("/profile/{id}")
    public String getStudentProfile(@PathVariable("id") int id, Model model) {
        Student student = studentServiceImpl.getStudentById(id);

        if (student == null) {
            return "error";  // Hoặc bạn có thể điều hướng đến một trang lỗi nếu không tìm thấy sinh viên
        }

        model.addAttribute("student", student);
        return "student_profile";
    }

    @GetMapping("/update-profile/{id}")
    public String showUpdateProfile(@PathVariable("id") int id, Model model) {
        Student student = studentServiceImpl.getStudentById(id);
        model.addAttribute("student", student);
        return "updateProfile";
    }

    // Xử lý khi người dùng submit update profile
    @PostMapping("/update/{id}")
    public String updateProfile(@PathVariable("id")int id, @ModelAttribute Student student) {
        studentServiceImpl.updateProfile(id, student);
        return "redirect:/student/profile/" + id;
    }

    @GetMapping("/change-password/{id}")
    public String showChangePassword(@PathVariable("id") int id, Model model) {
        Student student = studentServiceImpl.getStudentById(id);
        model.addAttribute("studentId", student.getStudentId());
        return "changePassword";
    }

    // Xử lý khi người dùng submit form đổi mật khẩu
    @PostMapping("/reset-password/{id}")
    public String resetPassword(@PathVariable("id") int id,
                                @RequestParam("oldPassword") String oldPassword,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New password and confirm password do not match.");
            return "changePassword";
        }

        boolean success = studentServiceImpl.changePassword(id, oldPassword, newPassword);
        if (!success) {
            model.addAttribute("error", "Old password is incorrect.");
            return "changePassword";
        }

        return "redirect:/student/profile/" + id;
    }



}
