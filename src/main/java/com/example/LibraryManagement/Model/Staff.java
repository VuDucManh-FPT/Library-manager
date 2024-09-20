package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @OneToOne
    @JoinColumn(name = "roleID")
    private Role role;
}
