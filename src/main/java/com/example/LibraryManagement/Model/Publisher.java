package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "Publisher")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int publisherId;
    @JoinColumn(name = "name")
    private String publisherName;
    @JoinColumn(name = "note")
    private String publisherNote;
    @OneToMany(mappedBy = "publisher")
    @ToString.Exclude
    private List<Book> books;
}
