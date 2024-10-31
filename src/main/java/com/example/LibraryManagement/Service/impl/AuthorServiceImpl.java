package com.example.LibraryManagement.Service.impl;

import com.example.LibraryManagement.Model.Author;
import com.example.LibraryManagement.Repository.AuthorRepository;
import com.example.LibraryManagement.Service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> findTop12() {
        return authorRepository.findTop12();
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

}
