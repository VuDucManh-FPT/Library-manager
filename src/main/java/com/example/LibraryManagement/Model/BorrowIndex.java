package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BorrowIndex")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowIndex {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int borrowIndexId;
    @ManyToOne
    @JoinColumn(name = "studentID",nullable = false,foreignKey = @ForeignKey(name = "FK_BookIndex_Student"))
    private Student student;
    @ManyToOne
    @JoinColumn(name = "staffid",nullable = false,foreignKey = @ForeignKey(name = "FK_BookIndex_Staff"))
    private Staff staff;
    @ManyToOne
    @JoinColumn(name = "bookid", nullable = false, foreignKey = @ForeignKey(name = "FK_BorrowIndex_Book"))
    private Book book;
    @ManyToOne
    @JoinColumn(name = "bookConditionBefore",nullable = false,foreignKey = @ForeignKey(name = "FK_BookIndex_BookCondition_Be"))
    private BookCondition conditionBefore;
    @ManyToOne
    @JoinColumn(name = "bookConditionAfter",nullable = false,foreignKey = @ForeignKey(name = "FK_BookIndex_BookCondition_Af"))
    private BookCondition conditionAfter;
    @JoinColumn(name = "estimateDate")
    private LocalDate estimateDate;
    @JoinColumn(name = "returnDate")
    private LocalDate returnDate;
    @JoinColumn(name = "startDate")
    private LocalDate startDate;

    @OneToMany(mappedBy = "borrowIndex")
    private List<BookFeedBack> feedbacks;
    @OneToMany(mappedBy = "borrowIndex")
    private List<BorrowFine> fines;
}
