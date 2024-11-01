package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Staff")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int staffId;
    @JoinColumn(name = "name")
    private String staffName;
    @JoinColumn(name = "email")
    private String staffEmail;
    @JoinColumn(name = "password")
    private String staffPassword;
    @ManyToOne
    @JoinColumn(name = "roleID", nullable = false, foreignKey = @ForeignKey(name = "FK_Staff_Role"))
    private Role role;
    @OneToMany(mappedBy = "staff",fetch = FetchType.EAGER)
    private List<BaseSalary> baseSalaries;
    @Column(name="dob")
    private LocalDate dob;
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
}
