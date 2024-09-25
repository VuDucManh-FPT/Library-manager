package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "BaseSalary")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseSalary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int baseSalaryId;
    @ManyToOne
    @JoinColumn(name = "staffID", nullable = false,foreignKey = @ForeignKey(name = "FK_BaseSalary_Staff"))
    private Staff staff;
    @JoinColumn(name = "startDate")
    private Date startDate;
    @JoinColumn(name = "endDate")
    private Date endDate;
}
