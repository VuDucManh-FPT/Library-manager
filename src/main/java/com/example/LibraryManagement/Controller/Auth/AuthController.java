package com.example.LibraryManagement.Controller.Auth;

import com.example.LibraryManagement.Model.*;
import com.example.LibraryManagement.Repository.*;
import com.example.LibraryManagement.Request.*;
import com.example.LibraryManagement.Security.JwtConstants;
import com.example.LibraryManagement.Security.JwtProvider;
import com.example.LibraryManagement.Service.AuthService;
import com.example.LibraryManagement.Service.ServiceImpl;
import com.example.LibraryManagement.Service.StudentServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/library")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServiceImpl serviceImpl;
    private final JwtProvider jwtProvider;

    @GetMapping("logout")
    public String logout(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        // Xóa JWT cookie
        Cookie jwtCookie = new Cookie(JwtConstants.JWT_COOKIE_NAME, null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie); // Thêm cookie vào phản hồi
        redirectAttributes.addFlashAttribute("message", "Bạn đã đăng xuất thành công!");
        return "redirect:/library/login";
    }

    @PostMapping("login")
    public String login(Model model, @ModelAttribute LoginRequest request, HttpServletResponse response) {
        String token = authService.login(model, request, response);
        if (token.equals("Bad credentials")) {
            model.addAttribute("error", "Password is not correct!");
            model.addAttribute("user", request);
            return "/Home/sign-in";
        }
        if (token.equals("System error")) {
            model.addAttribute("error", "System error!");
            model.addAttribute("user", request);
            return "/Home/sign-in";
        }
        if (token.equals("Username not exist!")) {
            model.addAttribute("error", "Username not exist");
            model.addAttribute("user", request);
            return "/Home/sign-in";
        }
        if (token.equals("Your account has been locked!")) {
            model.addAttribute("error", "Your account has been blocked!");
            model.addAttribute("user", request);
            return "/Home/sign-in";
        }
        if (token.equals("Your account never log before!")) {
            model.addAttribute("error", "Your account never log before!Please choose login with google");
            model.addAttribute("user", request);
            return "/Home/sign-in";
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = "";
        if (principal instanceof UserDetails) {
            role = ((UserDetails) principal).getAuthorities().toString();
        } else {
            // Xử lý nếu principal không phải là UserDetails hoặc String
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
        }
        if (role.equals("[STAFF]")) {
//            return "redirect:/staff/dashboard";
            return "redirect:/staff/rentals";
        }
        if (role.equals("[ADMIN]")) {
//            return "redirect:/staff/dashboard";
            return "redirect:/admin/accounts";
        }
        return "redirect:/library/home";
    }

    @GetMapping("login")
    public String signIn(Model model, HttpServletRequest request) {
        // Kiểm tra nếu có token JWT trong cookie
        String token = jwtProvider.getJwtFromCookies(request);

        if (token != null && jwtProvider.vailidateToken(token)) {
            // Lấy email của người dùng từ token
            String email = jwtProvider.getEmail(token);

            // Kiểm tra vai trò của người dùng
            Optional<Student> studentOpt = studentRepository.findByStudentEmail(email);
            Optional<Staff> staffOpt = staffRepository.findByStaffEmail(email);
            Optional<Admin> adminOpt = adminRepository.findAdminByEmail(email);

            if (studentOpt.isPresent()) {
                // Điều hướng đến trang chính của sinh viên
                return "redirect:/library/home";
            } else if (staffOpt.isPresent()) {
                // Điều hướng đến trang chính của nhân viên
                return "redirect:/staff/rentals";
            } else if (adminOpt.isPresent()) {
                // Điều hướng đến trang chính của admin
                return "redirect:/admin/dashboard";
            }
        }

        // Nếu không tìm thấy token hợp lệ, hiển thị trang đăng nhập
        model.addAttribute("user", new LoginRequest());
        return "/Home/sign-in";
    }


    @GetMapping("forgot-password")
    public String forgotPass(Model model) {
        if (!model.containsAttribute("error")) {
            model.addAttribute("error", "");
        }
        return "/Home/forgot-password";
    }

    @PostMapping("forgot-password")
    public String forgotPass(@ModelAttribute ForgotPassRequest forgotPassRequest, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        String output = "";
        Optional<Student> student = studentRepository.findByStudentEmail(forgotPassRequest.getEmail());
        Optional<Staff> staff = staffRepository.findByStaffEmail(forgotPassRequest.getEmail());
        Optional<Admin> admin = adminRepository.findAdminByEmail(forgotPassRequest.getEmail());
        if (student.isPresent()) {
            output = serviceImpl.sendMail(forgotPassRequest,"Your OTP is "+serviceImpl.generateRandom8DigitNumber(), "Forgot Password" );
        }
        if (staff.isPresent()) {
            output = serviceImpl.sendMail(forgotPassRequest,"Your OTP is "+serviceImpl.generateRandom8DigitNumber(), "Forgot Password" );
        }
        if (admin.isPresent()) {
            output = serviceImpl.sendMail(forgotPassRequest,"Your OTP is "+serviceImpl.generateRandom8DigitNumber(), "Forgot Password" );
        }
        redirectAttributes.addFlashAttribute("email", forgotPassRequest.getEmail());
        String otp = String.valueOf(serviceImpl.generateRandom8DigitNumber());
        Cookie otpCookie = new Cookie("otp", otp);
        otpCookie.setMaxAge(5 * 60); // Cookie có hiệu lực trong 5 phút
        otpCookie.setHttpOnly(true);
        otpCookie.setPath("/");
        response.addCookie(otpCookie);
        System.out.println(otpCookie);
        if (!student.isPresent() && !staff.isPresent() && !admin.isPresent()){
            redirectAttributes.addFlashAttribute("error","Email did not matched any account! Please try again.");
            return "redirect:/library/forgot-password";
        }
        if (output.equals("Success")) {
            redirectAttributes.addFlashAttribute("message", "We had sent you an email to reset your password!");
            return "redirect:/library/change-new-password";
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to send reset email. Please try again.");
            return "redirect:/library/forgot-password";
        }
    }

    @GetMapping("change-new-password")
    public String changeNewPass(Model model,@ModelAttribute("email") String email) {
        if (!model.containsAttribute("error")) {
            model.addAttribute("error", "");
        }
        model.addAttribute("email", email);
        return "/Home/change-new-password";
    }
    @PostMapping("change-new-password")
    public String resetPassword(@ModelAttribute NewPasswordRequest newPasswordRequest,RedirectAttributes redirectAttributes,@CookieValue(value = "otp", defaultValue = "") String otpFromCookie, HttpServletResponse response) {
        String enteredOtp = otpFromCookie;
        String email = newPasswordRequest.getEmail();
        Optional<Student> studentOpt = studentRepository.findByStudentEmail(email);
        Optional<Staff> staffOpt = staffRepository.findByStaffEmail(email);
        Optional<Admin> adminOpt = adminRepository.findAdminByEmail(email);
        if (!enteredOtp.equals(otpFromCookie)) {
            redirectAttributes.addFlashAttribute("error", "Invalid OTP. Please try again.");
            return "redirect:/library/change-new-password";
        }
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setPassword((passwordEncoder.encode(newPasswordRequest.getPassword())));
            studentRepository.save(student);
        }
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            staff.setStaffPassword((passwordEncoder.encode(newPasswordRequest.getPassword())));
            staffRepository.save(staff);
        }
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            admin.setPassword((passwordEncoder.encode(newPasswordRequest.getPassword())));
            adminRepository.save(admin);
        }
        System.out.println("Updating password for user: " + email);
        System.out.println("New password: " + newPasswordRequest.getPassword());

        Cookie otpCookie = new Cookie("otp", null);
        otpCookie.setMaxAge(0); // Đặt thời gian hết hạn là 0 để xóa cookie
        otpCookie.setPath("/");
        response.addCookie(otpCookie);
        redirectAttributes.addFlashAttribute("message", "Password had changed successful!");
        return "redirect:/library/login";
    }

}
