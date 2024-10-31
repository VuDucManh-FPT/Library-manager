package com.example.LibraryManagement.Service.impl;

import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Model.BookImage;
import com.example.LibraryManagement.Repository.BookImageRepository;
import com.example.LibraryManagement.Repository.BookRepository;
import com.example.LibraryManagement.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookImageRepository bookImageRepository;

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
        Book savedBook = bookRepository.save(book);
        List<BookImage> listImg = new ArrayList<>();
        // Process each image and save it
        for (MultipartFile image : images) {
            // Create a new BookImage instance
            BookImage bookImage = new BookImage();
            bookImage.setImageURL(saveImage(image)); // Store the path in the database
            bookImage.setBook(book); // Set the book for the image
            listImg.add(bookImage);
            bookImageRepository.save(bookImage);
        }
        savedBook.setBookImages(listImg);

        return bookRepository.save(savedBook); // Save the book to the database
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

    private String saveImage(MultipartFile image) {
        try {
            // Define an absolute path to an external directory
            String uploadDir = "D:/uploads/images"; // Update this path as needed
            File directory = new File(uploadDir);

            // Create the directory if it doesn't exist
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory and any necessary parent directories
            }

            // Create a unique filename to avoid conflicts
            String uniqueFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            String filePath = uploadDir + File.separator + uniqueFileName;

            // Save the file
            image.transferTo(new File(filePath));

            // Return the file path or URL as needed for use in your application
            return "D:/uploads/images/" + uniqueFileName; // Adjust this as needed for serving images
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle error appropriately
        }
    }

}
