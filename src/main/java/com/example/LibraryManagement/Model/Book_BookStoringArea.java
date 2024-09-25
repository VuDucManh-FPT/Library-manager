package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "Book_BookStoringArea")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book_BookStoringArea {
    @Id
    @ManyToOne
    @JoinColumn(name="bookID", nullable=false,foreignKey = @ForeignKey(name = "FK_Book_BookStoringArea_Book"))
    private Book book;
    @ManyToOne
    @JoinColumn(name = "bookStoringAreaID",nullable = false,foreignKey = @ForeignKey(name = "FK_Book_BookStoringArea_BookStoringArea"))
    private BookStoringArea bookStoringArea;
    @JoinColumn(name = "quantity")
    private int quantity;
    @JoinColumn(name = "section")
    private String section;
}
