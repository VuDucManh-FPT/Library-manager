package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "SalaryChange")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int salaryChangeId;
    @ManyToOne
    @JoinColumn(name="staffID")
    private Staff staff;
    @JoinColumn(name = "reason")
    private String reason;
    @JoinColumn(name = "value")
    private double value;
}
