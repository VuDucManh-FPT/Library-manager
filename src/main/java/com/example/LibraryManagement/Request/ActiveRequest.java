package com.example.LibraryManagement.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActiveRequest {
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private LocalDate dob;
    private String passwordConfirm;
    private String profileImage;
}
