package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Flow;

@Entity
@Table(name = "Book")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int bookId;
    @JoinColumn(name = "name")
    private String bookName;
    @JoinColumn(name = "description")
    private String bookDescription;
    @ManyToOne
    @JoinColumn(name = "publisherID",nullable = false, foreignKey = @ForeignKey(name = "FK_Book_Publisher"))
    private Publisher publisher;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookImage> bookImages;
    @OneToMany(mappedBy = "book")
    private List<Book_BookStoringArea> storingAreas;

    @ManyToMany
    @JoinTable(
            name = "Genre_Book",
            joinColumns = @JoinColumn(name = "bookID", foreignKey = @ForeignKey(name = "FK_Genre_Book_Book")),
            inverseJoinColumns = @JoinColumn(name = "gerneID", foreignKey = @ForeignKey(name = "FK_Genre_Book_Genre"))
    )
    private Set<Genre> genres = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "Book_Author",
            joinColumns = @JoinColumn(name = "bookID",foreignKey = @ForeignKey(name = "FK_Book_Author_Book")),
            inverseJoinColumns = @JoinColumn(name = "authorID",foreignKey = @ForeignKey(name = "FK_Book_Author_Author"))
    )
    private Set<Author> authors = new HashSet<>();

    @OneToOne(mappedBy = "book")
    private BorrowIndex borrowIndices;
}
