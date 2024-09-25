package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "BookFeedBack")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookFeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookFeedBackId;
    @Column(name = "feedbackDate")
    private Date feedbackDate;
    @Column(name = "detail")
    private String bookFeedBackDetail;
    @Column(name = "rating")
    private int rating;
    @ManyToOne
    @JoinColumn(name = "borrowIndexID", nullable = false,foreignKey = @ForeignKey(name = "FK_BookFeedBack_BorrowIndex"))
    private BorrowIndex borrowIndex;
}
