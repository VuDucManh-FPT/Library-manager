package com.example.LibraryManagement.Controller.Auth;

import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.StudentRepository;
import com.example.LibraryManagement.Request.ForgotPassRequest;
import com.example.LibraryManagement.Security.JwtProvider;
import com.example.LibraryManagement.Service.StudentService;
import com.example.LibraryManagement.Service.StudentServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final StudentRepository studentRepository;
    private final StudentServiceImpl studentServiceImpl;

    @GetMapping("/forgotPassword")
    public String forgotPass(Model model) {
        if (!model.containsAttribute("error")) {
            model.addAttribute("error", "");
        }
        return "/home/forgotPassword";
    }
    @PostMapping("/forgotPassword")
    public String forgotPass(@ModelAttribute ForgotPassRequest forgotPassRequest, RedirectAttributes redirectAttributes) {
        String output = "";
        Student student = studentRepository.findByStudentEmail(forgotPassRequest.getEmail());
        if (student != null) {
            output = studentServiceImpl.sendMail(student,"Your OTP is "+studentServiceImpl.generateRandom8DigitNumber(), "Forgot Password" );
        }
        if (student == null){
            redirectAttributes.addFlashAttribute("error","Email did not matched any account! Please try again.");
            return "redirect:/auth/forgotPassword";
        }
        if (output.equals("Success")) {
            redirectAttributes.addFlashAttribute("message", "We had sent you an email to reset your password!");
            return "redirect:/auth/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to send reset email. Please try again.");
            return "redirect:/auth/forgotPassword";
        }
    }
    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token) {
        String userEmail = JwtProvider.validateToken(token);

        if (userEmail != null) {
            return "Token is valid. You can now reset your password for: " + userEmail;
        } else {
            return "Token is invalid or expired.";
        }
    }
}
