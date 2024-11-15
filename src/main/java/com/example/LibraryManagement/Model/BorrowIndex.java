package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @ToString.Exclude
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
    @JoinColumn(name = "bookConditionAfter",foreignKey = @ForeignKey(name = "FK_BookIndex_BookCondition_Af"))
    private BookCondition conditionAfter;
    @JoinColumn(name = "estimateDate")
    private Date estimateDate;
    @JoinColumn(name = "returnDate")
    private Date returnDate;
    @JoinColumn(name = "startDate")
    private Date startDate;
    @OneToMany(mappedBy = "borrowIndex")
    private List<BookFeedBack> feedbacks;
    @OneToMany(mappedBy = "borrowIndex")
    @ToString.Exclude
    private List<BorrowFine> fines;
    @ManyToOne
    @JoinColumn(name = "updateStaffId", nullable = true, foreignKey = @ForeignKey(name = "FK_BorrowIndex_UpdateStaff"))
    @ToString.Exclude
    private Staff updateStaff;

    @ManyToOne
    @JoinColumn(name = "completeStaffId", nullable = true, foreignKey = @ForeignKey(name = "FK_BorrowIndex_CompleteStaff"))
    @ToString.Exclude
    private Staff completeStaff;

}
