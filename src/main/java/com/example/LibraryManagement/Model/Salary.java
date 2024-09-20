package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Salary")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int salaryId;
    @ManyToOne
    @JoinColumn(name = "staffID")
    private Staff staff;
    @JoinColumn(name = "salary")
    private double salary;
    @JoinColumn(name = "paymentDate")
    private Date paymentDate;
}
