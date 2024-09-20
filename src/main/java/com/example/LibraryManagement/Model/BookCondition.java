package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
}
