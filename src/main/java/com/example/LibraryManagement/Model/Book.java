package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.Flow;

@Entity
@Table(name = "Book")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;
    @JoinColumn(name = "name")
    private String bookName;
    @JoinColumn(name = "description")
    private String bookDescription;
    @ManyToOne
    @JoinColumn(name = "publisherID")
    private Publisher publisher;
}
