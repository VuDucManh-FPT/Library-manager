package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.*;
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
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;

    @Override
    public String login(Model model, LoginRequest request, HttpServletResponse response) {
        String email = request.getEmail();
        Optional<Student> studentOpt = studentRepository.findByStudentEmail(email);
        Optional<Staff> staffOpt = staffRepository.findByStaffEmail(email);
        Object user;
        if (studentOpt.isPresent()) {
            user = studentOpt.get();
            if (((Student) user).getAccountStates().getAccountStateId() == 3) {
                return "Your account has been locked!";
            }
            if (((Student) user).getAccountStates().getAccountStateId() == 1) {
                return "Your account never log before!";
            }
        } else if (staffOpt.isPresent()) {
            user = staffOpt.get(); // Lấy tài khoản nhân viên
            if (((Staff) user).getAccountStates().getAccountStateId() == 3) {
                return "Your account has been locked!";
            }
            if (((Staff) user).getAccountStates().getAccountStateId() == 1) {
                return "Your account never log before!";
            }
        } else {
            return "Username not exist!";
        }
        try {
            // Tạo UsernamePasswordAuthenticationToken dựa trên email và mật khẩu
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.generateToken(authentication);

            // Tạo cookie cho JWT
            ResponseCookie jwtCookie = jwtProvider.generateJwtCookie(user);

            // Thêm cookie vào phản hồi
            jwtProvider.addCookieToResponse(response, jwtCookie);
        } catch (UsernameNotFoundException e) {
            return "Bad credentials";
        } catch (BadCredentialsException ex) {
            return "Bad credentials";
        }

        return "Login success";
    }
}
