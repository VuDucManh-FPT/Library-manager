package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.*;
import com.example.LibraryManagement.Repository.AdminRepository;
import com.example.LibraryManagement.Repository.StaffRepository;
import com.example.LibraryManagement.Repository.StudentRepository;
import com.example.LibraryManagement.Request.LoginRequest;
import com.example.LibraryManagement.Security.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final AdminRepository adminRepository;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;

    @Override
    public String login(Model model, LoginRequest request, HttpServletResponse response) {
        String email = request.getEmail();
        Optional<Student> studentOpt = studentRepository.findByStudentEmail(email);
        Optional<Staff> staffOpt = staffRepository.findByStaffEmail(email);
        Optional<Admin> adminOpt = adminRepository.findAdminByEmail(email);
        Object user;

        if (studentOpt.isPresent()) {
            user = studentOpt.get();

            // Check if both inactive and banned
            if (!((Student) user).isActive() && ((Student) user).isIsban()) {
                return "System error";
            }

            // Check if only inactive
            if (!((Student) user).isActive()) {
                return "Your account is inactive!";
            }

            // Check if only banned
            if (((Student) user).isIsban()) {
                return "Your account has been banned!";
            }

        } else if (staffOpt.isPresent()) {
            user = staffOpt.get();

            // Check if both inactive and banned
            if (!((Staff) user).isActive() && ((Staff) user).isIsban()) {
                return "System error";
            }

            // Check if only inactive
            if (!((Staff) user).isActive()) {
                return "Your account is inactive!";
            }

            // Check if only banned
            if (((Staff) user).isIsban()) {
                return "Your account has been banned!";
            }

        } else if (adminOpt.isPresent()) {
            user = adminOpt.get();
        } else {
            return "Username does not exist!";
        }

        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.generateToken(authentication);

            // Create JWT cookie
            ResponseCookie jwtCookie = jwtProvider.generateJwtCookie(user);

            // Add cookie to the response
            jwtProvider.addCookieToResponse(response, jwtCookie);
        } catch (UsernameNotFoundException e) {
            return "Bad credentials";
        } catch (BadCredentialsException ex) {
            return "Bad credentials";
        }

        return "Login successful";
    }
}

