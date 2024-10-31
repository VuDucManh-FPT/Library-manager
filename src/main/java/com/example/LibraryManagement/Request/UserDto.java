package com.example.LibraryManagement.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String email;
    private String name;
    private Date dob;
    private String phoneNumber;
    private String avatar;
}
