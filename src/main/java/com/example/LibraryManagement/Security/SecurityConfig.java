package com.example.LibraryManagement.Security;

import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.StaffRepository;
import com.example.LibraryManagement.Repository.StudentRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, HttpSecurity httpSecurity) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/library/**","/oauth2/**","/staff/rentals/**").permitAll()
                        .requestMatchers("/student/**").hasAnyAuthority("STUDENT")
                        .requestMatchers("/staff/**").hasAnyAuthority("STAFF")
                        .anyRequest().permitAll())
         // oauth2
        .oauth2Login(oauth2 -> oauth2
                .loginPage("/library/login")
                .successHandler(customOauth2SuccessHandler())
        );
        return http.build();
    }
    public AuthenticationSuccessHandler customOauth2SuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                // Kiểm tra xem authentication có phải là OAuth2AuthenticationToken không
                if (authentication instanceof OAuth2AuthenticationToken) {
                    OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;

                    // Lấy email từ các thuộc tính của người dùng
                    String email = (String) oauth2Token.getPrincipal().getAttributes().get("email");

                    log.info("Attempting to find user with email: {}", email);

                    Optional<Student> studentOpt = studentRepository.findByStudentEmail(email);
                    Optional<Staff> staffOpt = staffRepository.findByStaffEmail(email);

                    if (studentOpt.isPresent()) {
                        Student student = studentOpt.get();
                        handleRedirectBasedOnAccountState(student.getAccountStates().getAccountStateId(), response, "STUDENT");
                    } else if (staffOpt.isPresent()) {
                        Staff staff = staffOpt.get();
                        handleRedirectBasedOnAccountState(staff.getAccountStates().getAccountStateId(), response, "STAFF");
                    } else {
                        log.warn("User '{}' not found in the system after OAuth2 login.", email);
                        response.sendRedirect("/library/login?error=user-not-found");
                    }
                } else {
                    log.warn("Authentication is not an instance of OAuth2AuthenticationToken.");
                    response.sendRedirect("/library/login?error=authentication-failed");
                }
            }
            private void handleRedirectBasedOnAccountState(int accountStateId, HttpServletResponse response, String role) throws IOException {
            if (accountStateId == 1) {
                // Chuyển hướng đến trang cập nhật hồ sơ
                response.sendRedirect("/library/profile-update");
            } else if (accountStateId == 2) {
                if (role.equals("STUDENT")) {
                    // Chuyển hướng đến trang chủ cho học sinh
                    response.sendRedirect("/library/home");
                } else if (role.equals("STAFF")) {
                    // Chuyển hướng đến trang dashboard cho nhân viên
//                    response.sendRedirect("/staff/dashboard");
                    response.sendRedirect("/staff/rentals");
                }
            } else {
                response.sendRedirect("/library/login?error=account-locked");
            }
        }
};
}
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


