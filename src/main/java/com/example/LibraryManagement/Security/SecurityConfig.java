package com.example.LibraryManagement.Security;

import com.example.LibraryManagement.Model.Admin;
import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.AdminRepository;
import com.example.LibraryManagement.Repository.StaffRepository;
import com.example.LibraryManagement.Repository.StudentRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseCookie;
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
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final AdminRepository adminRepository;
    private JwtProvider jwtProvider;


    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, HttpSecurity httpSecurity) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/library/**","/oauth2/**").permitAll()
                        .requestMatchers("/student/**").hasAnyAuthority("STUDENT")
                        .requestMatchers("/staff/**").hasAnyAuthority("STAFF")
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                        .anyRequest().permitAll())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Thêm bộ lọc JWT

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
                    Optional<Admin> adminOpt = adminRepository.findAdminByEmail(email);
                    Object user = new Object();
                    if (studentOpt.isPresent()) {
                        Student student = studentOpt.get();
                        user = studentOpt.get();
                        handleRedirectBasedOnAccountFlags(student.isActive(), student.isIsban(), response, "STUDENT");
                    } else if (staffOpt.isPresent()) {
                        Staff staff = staffOpt.get();
                        user = staffOpt.get();
                        handleRedirectBasedOnAccountFlags(staff.isActive(), staff.isIsban(), response, "STAFF");
                    } else if (adminOpt.isPresent()) {
                        Admin admin = adminOpt.get();
                        user = adminOpt.get();
                        response.sendRedirect("/admin/staffs");
                    }else {
                        log.warn("User '{}' not found in the system after OAuth2 login.", email);
                        response.sendRedirect("/library/login?error=user-not-found");
                    }
                    // Tạo token JWT
                    String token = jwtProvider.generateToken(authentication);
                    // Tạo cookie từ token
                    ResponseCookie jwtCookie = jwtProvider.generateJwtCookie(user);
                    // Thêm cookie vào response
                    jwtProvider.addCookieToResponse(response, jwtCookie);
                } else {
                    log.warn("Authentication is not an instance of OAuth2AuthenticationToken.");
                    response.sendRedirect("/library/login?error=authentication-failed");
                }
            }
            private void handleRedirectBasedOnAccountFlags(boolean isActive, boolean isBanned, HttpServletResponse response, String role) throws IOException {
                if (!isActive && isBanned) {
                    // Both inactive and banned
                    response.sendRedirect("/library/login?error=system-error");
                } else if (!isActive) {
                    //Staff first time vao update profile
                    // Inactive account
                    response.sendRedirect("/library/profile-update");
                } else if (isBanned) {
                    // Banned account
                    response.sendRedirect("/library/login?error=account-banned");
                } else {
                    // Redirect based on role
                    if (role.equals("STUDENT")) {
                        response.sendRedirect("/library/home");
                    } else if (role.equals("STAFF")) {
                        response.sendRedirect("/staff/rentals");
                    }
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


