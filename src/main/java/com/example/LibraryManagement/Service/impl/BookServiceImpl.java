package com.example.LibraryManagement.Service.impl;

import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Model.BookImage;
import com.example.LibraryManagement.Model.Genre;
import com.example.LibraryManagement.Repository.BookImageRepository;
import com.example.LibraryManagement.Repository.BookRepository;
import com.example.LibraryManagement.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookImageRepository bookImageRepository;
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookImageRepository bookImageRepository) {
        this.bookRepository = bookRepository;
        this.bookImageRepository = bookImageRepository;
    }

    @Override
    public List<Book> getTop12Book() {
        return bookRepository.findTop12();
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> searchBook(String bookName) {
        return bookRepository.findByBookNameContaining(bookName);
    }

    @Override
    public Book getById(int id) {
        return bookRepository.findById(id).orElseThrow();
    }

    @Override
    public Book update(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book addNew(Book book, List<MultipartFile> images) {
        if (book.getBookId() != 0) { // Assuming 0 is used for new books
            Book existingBook = bookRepository.findById(book.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            // Retain existing images
            List<BookImage> existingImages = existingBook.getBookImages();

            // If the existingImages is null, initialize it
            if (existingImages == null) {
                existingImages = new ArrayList<>();
            }

            // Set the existing images back to the book
            book.setBookImages(existingImages);
        }

        // Process each new image and create BookImage instances
        List<BookImage> listImg = book.getBookImages();
        for (MultipartFile image : images) {
            BookImage bookImage = new BookImage();
            bookImage.setImageURL(saveImage(image)); // Store the path in the database
            bookImage.setBook(book); // Set the book for the image
            listImg.add(bookImage); // Add new image to the list
        }

        // Persist the book (and images due to cascade)
        return bookRepository.save(book); // Save the book, including images
    }

    @Override
    public void delete(int bookId) {
        bookRepository.deleteById(bookId);
    }

    @Override
    public List<Book> findBooksInSameGenre(Integer bookId, int limit) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return bookRepository.findTopByGenres(book.getGenres(), bookId, PageRequest.of(0, limit));
    }

    public String saveImage(MultipartFile image) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            Path path = Paths.get(uploadDir, fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, image.getBytes());
            return "/uploads/" + fileName;  // Assuming '/uploads/' is the base URL for your images
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }

}
