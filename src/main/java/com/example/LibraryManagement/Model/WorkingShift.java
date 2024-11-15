package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "WorkingShift")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workingShiftId;
    @JoinColumn(name = "startDate")
    private Date startDate;
    @JoinColumn(name = "endDate")
    private Date endDate;
    @OneToMany(mappedBy = "workingShift")
    private List<WorkingSchedule> workingSchedules;
}
