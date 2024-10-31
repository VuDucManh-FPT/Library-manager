package com.example.LibraryManagement.Service.impl;

import com.example.LibraryManagement.Model.Genre;
import com.example.LibraryManagement.Repository.GenreRepository;
import com.example.LibraryManagement.Service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }
}
