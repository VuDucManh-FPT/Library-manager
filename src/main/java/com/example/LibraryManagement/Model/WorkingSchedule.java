package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "WorkingSchedule")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workScheduleId;
    @ManyToOne
    @JoinColumn(name = "workingShiftID", nullable = false, foreignKey = @ForeignKey(name = "FK_WorkingSchedule_WorkingShift"))
    private WorkingShift workingShift;
    @JoinColumn(name = "workingDate")
    private Date workingDate;
    @ManyToOne
    @JoinColumn(name = "staffID", nullable = false,foreignKey = @ForeignKey(name = "FK_WorkingSchedule_Staff"))
    private Staff staff;
}
