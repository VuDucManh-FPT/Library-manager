package com.example.LibraryManagement.Controller.Profile;

import com.example.LibraryManagement.Model.Admin;
import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/library")
@AllArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profile")
    public String showProfilePage(Model model, HttpServletRequest request) {
        String email = profileService.getUserEmailFromCookie(request);
        Optional<Object> userOpt = profileService.getUserByEmail(email);

        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            if (userOpt.get() instanceof Student) {
                model.addAttribute("role", "STUDENT");
            } else if (userOpt.get() instanceof Staff) {
                model.addAttribute("role", "STAFF");
            } else if (userOpt.get() instanceof Admin) {
                model.addAttribute("role", "ADMIN");
            }
            return "/Profile/profile";
        }

        model.addAttribute("error", "User not found");
        return "/error";
    }

    @GetMapping("/update-profile")
    public String showProfileUpdate(Model model, HttpServletRequest request) {
        String email = profileService.getUserEmailFromCookie(request);
        Optional<Object> userOpt = profileService.getUserByEmail(email);

        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            if (userOpt.get() instanceof Student) {
                model.addAttribute("role", "STUDENT");
            } else if (userOpt.get() instanceof Staff) {
                model.addAttribute("role", "STAFF");
            } else if (userOpt.get() instanceof Admin) {
                model.addAttribute("role", "ADMIN");
            }
            return "/Profile/update-profile";
        }

        model.addAttribute("error", "User not found");
        return "/error";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@RequestParam("fullName") String fullName,
                                @RequestParam("dob") LocalDate dob,
                                @RequestParam("phoneNumber") String phoneNumber,
                                @RequestParam("address") String address,
                                @RequestParam("avatar") MultipartFile avatarFile,
                                @RequestParam("age") int age,
                                @RequestParam("gender") String gender,
                                HttpServletRequest request) {

        String email = profileService.getUserEmailFromCookie(request);
        String avatarUrl = profileService.handleAvatarUpload(avatarFile);

        profileService.getUserByEmail(email).ifPresent(user -> {
            if (user instanceof Student) {
                profileService.updateStudentProfile((Student) user, fullName, dob, phoneNumber, address, age, gender, avatarUrl);
            } else if (user instanceof Staff) {
                profileService.updateStaffProfile((Staff) user, fullName, dob, phoneNumber, address, age, gender, avatarUrl);
            }
        });

        return "redirect:/library/profile";
    }
}
