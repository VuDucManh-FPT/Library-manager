package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.Book;

import java.util.List;

public interface BookService {
    List<Book> getTop12Book();
}
