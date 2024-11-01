package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "BorrowFine")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowFine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int borrowFineId;
    @ManyToOne
    @JoinColumn(name = "borrowIndexID", nullable = false,foreignKey = @ForeignKey(name = "FK_BorrowFine_BorrowIndex"))
    private BorrowIndex borrowIndex;
    @Column(name = "reason")
    private String reason;
    @Column(name = "value")
    private int value;
    @Column(name = "status")
    private String status;
}
