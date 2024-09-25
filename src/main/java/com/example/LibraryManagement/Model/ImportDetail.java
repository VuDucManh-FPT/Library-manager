package com.example.LibraryManagement.Model;import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "ImportDetail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private int price;
    @Column(name = "vat")
    private float vat;

    @ManyToOne
    @JoinColumn(name = "bookImportID", nullable = false, foreignKey = @ForeignKey(name = "FK_ImportDetail_BookImport"))
    private BookImport bookImport;

    @ManyToOne
    @JoinColumn(name = "bookID", nullable = false,foreignKey = @ForeignKey(name = "FK_ImportDetail_Book"))
    private Book book;
}
