package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Model.BookCondition;
import com.example.LibraryManagement.Model.BorrowIndex;
import com.example.LibraryManagement.Model.Student;

import java.util.List;

public interface BorrowIndexService {
    List<BorrowIndex> getAllBorrowIndex();
    List<Student> getAllStudents();
    List<BookCondition> getAllBookConditions();
    List<Book> getAllBooks();
    BorrowIndex getBorrowIndexById(int id);
}
