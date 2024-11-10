package com.example.LibraryManagement.Controller.Admin;

import com.example.LibraryManagement.Model.Admin;
import com.example.LibraryManagement.Repository.AdminRepository;
import com.example.LibraryManagement.Response.NavbarResponse;
import com.example.LibraryManagement.Security.JwtProvider;
import com.example.LibraryManagement.Service.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class ProfileAdminController {
    private final ServiceImpl serviceImpl;
    private final AdminRepository adminRepository;
    private final JwtProvider jwtProvider;
    @GetMapping("/edit")
    public String edit(Model model, HttpServletRequest request) {
        NavbarResponse navbarData = serviceImpl.getNavbarAdminData(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        return "Admin/admin-profile";
    }
    @PostMapping("/edit")
    public String edit(RedirectAttributes redirectAttributes, HttpServletRequest request,
                       @RequestParam String email, HttpServletResponse response) {
        NavbarResponse navbarData = serviceImpl.getNavbarAdminData(request);
        Optional<Admin> admin = adminRepository.findAdminByEmail(navbarData.email);
        admin.get().setEmail(email);
        adminRepository.save(admin.get());
        String token = jwtProvider.generateTokenByMail(email);
        ResponseCookie jwtCookie = jwtProvider.generateJwtCookie(admin.get());
        jwtProvider.addCookieToResponse(response, jwtCookie);
        redirectAttributes.addFlashAttribute("success", "Update success");
        return "redirect:/admin/edit";
    }
}
