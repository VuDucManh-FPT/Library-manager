package com.example.LibraryManagement.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.ToString;

@Entity
@Table(name = "BookImage")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "imageURL", length = 255, nullable = false)
    private String imageURL;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "bookID", nullable = false, foreignKey = @ForeignKey(name = "FK_BookImage_Book"))
    private Book book;
}
