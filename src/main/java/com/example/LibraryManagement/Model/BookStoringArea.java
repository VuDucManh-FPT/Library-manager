package com.example.LibraryManagement.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "BookStoringArea")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookStoringArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookStoringAreaId;
    @JoinColumn(name = "name")
    private String bookStoringAreaName;
    @JoinColumn(name = "description")
    private String bookStoringAreaDescription;
}
