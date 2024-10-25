package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "BookCondition")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookConditionId;
    @JoinColumn(name = "detail")
    private String detail;
    @OneToMany(mappedBy = "conditionBefore")
    @ToString.Exclude
    private List<BorrowIndex> borrowIndexesBefore;

    @OneToMany(mappedBy = "conditionAfter")
    @ToString.Exclude
    private List<BorrowIndex> borrowIndexesAfter;
}
