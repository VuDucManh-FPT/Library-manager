package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.Book;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    List<Book> getTop12Book();
    List<Book> getAll();
    List<Book> searchBook(String bookName);
    Book getById(int id);
    Book update(Book book);
    Book addNew(Book book, List<MultipartFile> images);
    void delete(int bookId);

    List<Book> findBooksInSameGenre(Integer bookId, int limit);
}
