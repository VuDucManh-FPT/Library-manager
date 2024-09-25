package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "staffID",nullable = false,foreignKey = @ForeignKey(name = "FK_BookIndex_Staff"))
    private Staff staff;
    @OneToMany
    @JoinColumn(name = "bookID")
    private List<Book> books;
    @ManyToOne
    @JoinColumn(name = "bookConditionBefore",nullable = false,foreignKey = @ForeignKey(name = "FK_BookIndex_BookCondition_Be"))
    private BookCondition conditionBefore;
    @ManyToOne
    @JoinColumn(name = "bookConditionAfter",nullable = false,foreignKey = @ForeignKey(name = "FK_BookIndex_BookCondition_Af"))
    private BookCondition conditionAfter;
    @JoinColumn(name = "estimateDate")
    private Date estimateDate;
    @JoinColumn(name = "returnDate")
    private Date returnDate;
    @OneToMany(mappedBy = "borrowIndex")
    private List<BookFeedBack> feedbacks;
    @OneToMany(mappedBy = "borrowIndex")
    private List<BorrowFine> fines;
}
