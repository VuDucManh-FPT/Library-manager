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

    @GetMapping("/profile")
    public String getProfilePage(HttpServletRequest request, Model model) {
        Student studentRequesting = studentServiceImpl.getStudentRequesting(request);
        model.addAttribute("student", studentRequesting);
        return "profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(HttpServletRequest http, Model model,
                                @Valid @ModelAttribute("name") String name,
                                @Valid @ModelAttribute("email") String email,
                                RedirectAttributes redirectAttributes, BindingResult result) {
        Student studentRequesting = studentServiceImpl.getStudentRequesting(http);
        studentRequesting.setStudentName(name);
        studentRequesting.setStudentEmail(email);
        studentServiceImpl.save(studentRequesting);
        model.addAttribute("student", studentRequesting);
        String successMessage = "Updated information successfully!";
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        return "redirect:/profile";
    }

    @PostMapping("/update-password")
    public String updatePassword(HttpServletRequest request, Model model,
                                 @RequestParam("oldPass") String oldPass,
                                 @RequestParam("newPass") String newPass,
                                 @RequestParam("reNewPass") String reNewPass,
                                 RedirectAttributes redirectAttributes) {
        Student student = studentServiceImpl.getStudentRequesting(request);

        if (!oldPass.equals(student.getPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Old password is incorrect.");
            return "redirect:/profile?changePassword=true";
        }

        if (!newPass.equals(reNewPass)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match.");
            return "redirect:/profile?changePassword=true";
        }

        student.setPassword(newPass);
        studentServiceImpl.save(student);
        redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        return "redirect:/profile";
    }

}
