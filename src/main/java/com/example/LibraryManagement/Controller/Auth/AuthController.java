package com.example.LibraryManagement.Controller.Auth;

import com.example.LibraryManagement.Model.*;
import com.example.LibraryManagement.Repository.*;
import com.example.LibraryManagement.Request.*;
import com.example.LibraryManagement.Security.JwtConstants;
import com.example.LibraryManagement.Security.JwtProvider;
import com.example.LibraryManagement.Service.AuthService;
import com.example.LibraryManagement.Service.ServiceImpl;
import com.example.LibraryManagement.Service.StudentServiceImpl;
import com.example.LibraryManagement.Utils.FileUploadUtil;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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
        response.addCookie(jwtCookie);
        return "redirect:/library/login";
    }

    @PostMapping("login")
    public String login(Model model, @ModelAttribute LoginRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
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
        if (token.equals("Your account has been banned!")) {
            model.addAttribute("error", "Your account has been blocked!");
            model.addAttribute("user", request);
            return "/Home/sign-in";
        }
        if (token.equals("Your account is inactive!")) {
            redirectAttributes.addFlashAttribute("user", request);
            return "redirect:/library/active";
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal == null) {
            System.out.println("principal is null");
        }
        String role = "";
        String email ="";
        if (principal instanceof UserDetails) {
            role = ((UserDetails) principal).getAuthorities().toString();
            email =((UserDetails) principal).getUsername().toString();

            System.out.println("email:"+email);
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
            return "redirect:/admin/staffs";
        }
        return "redirect:/library/home";
    }
    @PostMapping("/active")
    public String activateAccount(@ModelAttribute ActiveRequest activationRequest,HttpServletRequest request,
                                  RedirectAttributes redirectAttributes, Model model, @RequestParam("file") MultipartFile file) {
        String uploadDir = "src/main/resources/static/BookSto/assets/images/avatars";
        try {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                FileUploadUtil.saveFile(uploadDir, fileName, file);
                activationRequest.setProfileImage(fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addAttribute("error", "Error occurred while storing the image!");
            return "redirect:/library/login";
        }
        boolean isActivated = authService.activateAccount(activationRequest);

        if (isActivated) {
            String token = jwtProvider.getJwtFromCookies(request);
            String email = jwtProvider.getEmail(token);

            // Kiểm tra vai trò của người dùng
            Optional<Student> studentOpt = studentRepository.findByStudentEmail(email);
            Optional<Staff> staffOpt = staffRepository.findByStaffEmail(email);
            Optional<Admin> adminOpt = adminRepository.findAdminByEmail(email);

            if (studentOpt.isPresent()) {
                return "redirect:/library/home";
            } else if (staffOpt.isPresent()) {
                return "redirect:/staff/rentals";
            } else if (adminOpt.isPresent()) {
                return "redirect:/admin/staffs";
            }
        } else {
            model.addAttribute("user", activationRequest);
            model.addAttribute("error", "Failed to activate account. Please try again.");
            return "/Home/active-acount";
        }
        return "redirect:/library/login";
    }
    @GetMapping("/active")
    public String showActivationPage(Model model,RedirectAttributes redirectAttributes) {
        LoginRequest loginRequest = (LoginRequest) model.asMap().get("user");
        if (loginRequest != null) {
            String email = loginRequest.getEmail();
            Optional<Student> studentOpt = studentRepository.findByStudentEmail(email);
            Optional<Staff> staffOpt = staffRepository.findByStaffEmail(email);
            ActiveRequest activeRequest = new ActiveRequest();
            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                activeRequest.setName(student.getStudentName());
                activeRequest.setEmail(student.getStudentEmail());
                activeRequest.setAddress(student.getAddress());
                activeRequest.setPhoneNumber(student.getPhoneNumber());
                activeRequest.setDob(student.getDob());
                model.addAttribute("user", activeRequest);
                return "/Home/active-acount";
            }
            if (staffOpt.isPresent()) {
                Staff staff = staffOpt.get();
                activeRequest.setName(staff.getStaffName());
                activeRequest.setEmail(staff.getStaffEmail());
                activeRequest.setAddress(staff.getAddress());
                activeRequest.setPhoneNumber(staff.getPhoneNumber());
                activeRequest.setDob(staff.getDob());
                model.addAttribute("user", activeRequest);
                return "/Home/active-acount";
            }
        }
        if (loginRequest != null) {
            redirectAttributes.addFlashAttribute("error", "Some thing wrong please login again.");
            return "redirect:/library/login";
        }
        return "/Home/active-acount";
    }
    @GetMapping("login")
    public String signIn(Model model, HttpServletRequest request,@RequestParam(value = "error", required = false) String error) {
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
                return "redirect:/admin/staffs";
            }
        }
        if (error != null) {
            // Thêm thông báo lỗi vào model để hiển thị trên trang đăng nhập
            switch (error) {
                case "user-not-found":
                    model.addAttribute("error", "Email not found in the system.");
                    break;
                case "authentication-failed":
                    model.addAttribute("error", "Authentication failed.");
                    break;
                case "account-banned":
                    model.addAttribute("error", "Account has been banned.");
                    break;
                default:
                    model.addAttribute("error", "There seems to be an error.");
                    break;
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

    @GetMapping("signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("student", new Student());
        return "Home/sign-up";
    }
    @PostMapping("signup")
    public String registerStudent(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        student.setActive(true);
        student.setIsban(false);
        studentRepository.save(student);

        redirectAttributes.addFlashAttribute("message", "Register successful!.");
        return "redirect:/library/login";
    }




}
