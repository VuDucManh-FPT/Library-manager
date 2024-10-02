package com.example.LibraryManagement.Service.impl;

import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Repository.BookRepository;
import com.example.LibraryManagement.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getTop5Book() {
        return bookRepository.findTop5();
    }
}
