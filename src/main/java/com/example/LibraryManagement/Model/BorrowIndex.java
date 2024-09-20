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
    @JoinColumn(name = "studentID")
    private Student student;
    @OneToMany
    @JoinColumn(name = "bookID")
    private List<Book> books;
    @OneToMany
    @JoinColumn(name = "bookConditionBefore")
    private List<BookCondition> bookConditionsBe;
    @OneToMany
    @JoinColumn(name = "bookConditionAfter")
    private List<BookCondition> bookConditionsAf;
    @JoinColumn(name = "estimateDate")
    private Date estimateDate;
    @JoinColumn(name = "returnDate")
    private Date returnDate;

}
