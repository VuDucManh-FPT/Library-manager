package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;
    @Override
    public List<Book> getTop12Book() {
        return bookRepository.findTop12();
    }

    @Override
    public void setInActiveBook(Book book) {
        book.setActive(false);
        bookRepository.save(book);
    }
}
