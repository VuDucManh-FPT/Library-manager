package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @JoinColumn(name = "publisherID",nullable = true, foreignKey = @ForeignKey(name = "FK_Book_Publisher"))
    @ToString.Exclude
    private Publisher publisher;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<BookImage> bookImages;
    @OneToMany(mappedBy = "book")
    @ToString.Exclude
    private List<Book_BookStoringArea> storingAreas;

    @ManyToMany
    @JoinTable(
            name = "Genre_Book",
            joinColumns = @JoinColumn(name = "bookID", foreignKey = @ForeignKey(name = "FK_Genre_Book_Book")),
            inverseJoinColumns = @JoinColumn(name = "gerneID", foreignKey = @ForeignKey(name = "FK_Genre_Book_Genre"))
    )
    @ToString.Exclude
    private Set<Genre> genres = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "Book_Author",
            joinColumns = @JoinColumn(name = "bookID",foreignKey = @ForeignKey(name = "FK_Book_Author_Book")),
            inverseJoinColumns = @JoinColumn(name = "authorID",foreignKey = @ForeignKey(name = "FK_Book_Author_Author"))
    )
    @ToString.Exclude
    private Set<Author> authors = new HashSet<>();
    @JoinColumn(name = "price")
    private float price;
    @OneToMany(mappedBy = "book")
    @ToString.Exclude
    private List<ImportDetail> importDetails;
    @Transient
    private String firstImageName;
    @JoinColumn(name = "isActive", nullable = true)
    private boolean isActive;
}
