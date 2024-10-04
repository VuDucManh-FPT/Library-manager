package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Request.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;

public interface AuthService{
    String login(Model model, LoginRequest request, HttpServletResponse response);
}
