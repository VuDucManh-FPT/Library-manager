package com.example.LibraryManagement.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Student")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentId;
    @Column(name = "name")
    private String studentName;
    @Column(name = "email")
    private String studentEmail;
    @Column(name="password")
    private String password;
    @Column(name="dob")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "active", columnDefinition = "BIT")
    private boolean active;

    @Column(name = "isban", columnDefinition = "BIT")
    private boolean isban;
    @Column(name = "gender")
    private String gender;
    @Column(name ="age")
    private int age;


}
