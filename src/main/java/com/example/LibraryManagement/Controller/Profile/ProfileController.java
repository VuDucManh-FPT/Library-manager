package com.example.LibraryManagement.Controller.Profile;

import com.example.LibraryManagement.Model.Admin;
import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.AdminRepository;
import com.example.LibraryManagement.Repository.StaffRepository;
import com.example.LibraryManagement.Repository.StudentRepository;
import com.example.LibraryManagement.Security.JwtProvider;
import com.example.LibraryManagement.Security.SecurityConfig;
import com.example.LibraryManagement.Service.AuthService;
import com.example.LibraryManagement.Service.ServiceImpl;
import com.example.LibraryManagement.Service.UserServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/library")
@AllArgsConstructor
public class ProfileController {
    private final AuthService authService;
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServiceImpl serviceImpl;
    private final JwtProvider jwtProvider;
    private final UserServiceImpl userServiceImpl;
    private final SecurityConfig securityConfig;

    @GetMapping("/profile")
    public String showProfilePage(Model model, HttpServletRequest request) {

        String email = getUserEmailFromCookie(request);

        // Lấy thông tin người dùng từ cơ sở dữ liệu
        Optional<Student> studentOpt = studentRepository.findByStudentEmail(email);
        Optional<Staff> staffOpt = staffRepository.findByStaffEmail(email);
        Optional<Admin> adminOpt = adminRepository.findAdminByEmail(email);

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            model.addAttribute("user", student);
            model.addAttribute("role", "STUDENT");
        } else if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            model.addAttribute("user", staff);
            model.addAttribute("role", "STAFF");
        } else if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            model.addAttribute("user", admin);
            model.addAttribute("role", "ADMIN");
        } else {
            model.addAttribute("error", "User not found");
        }

        return "/Profile/profile"; // Tên view để hiển thị thông tin
    }

    @GetMapping("/update-profile")
    public String showProfileUpdate(Model model, HttpServletRequest request) {

        String email = getUserEmailFromCookie(request);

        // Lấy thông tin người dùng từ cơ sở dữ liệu
        Optional<Student> studentOpt = studentRepository.findByStudentEmail(email);
        Optional<Staff> staffOpt = staffRepository.findByStaffEmail(email);
        Optional<Admin> adminOpt = adminRepository.findAdminByEmail(email);

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            model.addAttribute("user", student);

            model.addAttribute("role", "STUDENT");
        } else if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            model.addAttribute("user", staff);
            model.addAttribute("fullName", staff.getStaffName());
            model.addAttribute("role", "STAFF");
        } else if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            model.addAttribute("user", admin);
            model.addAttribute("role", "ADMIN");
        } else {
            model.addAttribute("error", "User not found");
        }

        return "/Profile/update-profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@RequestParam("fullName") String fullName,
                                @RequestParam("dob") LocalDate dob,
                                @RequestParam("phoneNumber") String phoneNumber,
                                @RequestParam("address") String address,
                                @RequestParam("avatar") MultipartFile avatarFile,
                                HttpServletRequest request, Model model) {
        String email = getUserEmailFromCookie(request);
        Optional<Student> studentOpt = studentRepository.findByStudentEmail(email);
        Optional<Staff> staffOpt = staffRepository.findByStaffEmail(email);

        String avatarUrl = null;
//        if (!avatarFile.isEmpty()) {
//            try {
//                // Kiểm tra thư mục upload
//                String uploadDir = "/static/uploads/";
//                File directory = new File(uploadDir);
//                if (!directory.exists()) {
//                    directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
//                }
//
//                // Lưu file ảnh avatar vào server
//                String originalFileName = avatarFile.getOriginalFilename();
//                String newFileName = System.currentTimeMillis() + "_" + originalFileName; // Thêm timestamp vào tên file
//                File uploadFile = new File(uploadDir + newFileName);
//                avatarFile.transferTo(uploadFile);
//
//                avatarUrl = uploadFile.getPath();
//                if (avatarUrl.isEmpty()) {
//                    System.out.println("no avatar found");
//                } else {
//                    System.out.println("avatar found");
//                }
//            } catch (IOException e) {
//                model.addAttribute("error", "Lỗi khi upload ảnh: " + e.getMessage());
//                System.out.println("Lỗi upload ảnh: " + e.getMessage()); // In ra thông tin lỗi chi tiết
//                return "Profile/update-profile";
//            }
//        } else {
//            System.out.println("no avatar file");
//        }


        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setStudentName(fullName.trim().isEmpty() ? student.getStudentName() : fullName);
            student.setDob(dob != null ? dob : student.getDob());
            student.setPhoneNumber(phoneNumber.trim().isEmpty() ? student.getPhoneNumber() : phoneNumber);
            student.setAddress(address.trim().isEmpty() ? student.getAddress() : address);
            if (avatarUrl != null) {
                student.setAvatar(avatarUrl);
            }
            studentRepository.save(student);
        } else if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            staff.setStaffName(fullName.trim().isEmpty() ? staff.getStaffName() : fullName);
            staff.setDob(dob != null ? dob : staff.getDob());
            staff.setPhoneNumber(phoneNumber.trim().isEmpty() ? staff.getPhoneNumber() : phoneNumber);
            staff.setAddress(address.trim().isEmpty() ? staff.getAddress() : address);
            if (avatarUrl != null) {
                staff.setAvatar(avatarUrl);
            }
            staffRepository.save(staff);
        }

        return "redirect:/library/profile";
    }
    private String getUserEmailFromCookie(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if ("userEmail".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

//    @GetMapping
//    ApiResponse<list<UserResponse>>getUsers getUsers(){
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        log.info("username:{}",authentication.getName());
//        log.info("role:{}",authentication.getAuthorities().)
//        return Apiresponse.<->builder().result(userServiceImpl.getUsers()).build();
//    }
}
