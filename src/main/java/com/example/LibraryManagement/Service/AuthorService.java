package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findTop5();
}
