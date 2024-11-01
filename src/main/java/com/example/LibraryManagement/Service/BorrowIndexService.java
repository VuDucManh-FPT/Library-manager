package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.*;

import java.util.List;

public interface BorrowIndexService {
    List<BorrowIndex> getAllBorrowIndex();
    List<Student> getAllStudents();
    List<BookCondition> getAllBookConditionsAdd();
    List<Book> getAllBooks();
    BorrowIndex getBorrowIndexById(int id);
    List<BookCondition> getAllBookConditionsComplete(int id);
    BorrowFine getBorrowFineByBorrowIndex(BorrowIndex borrowIndex);
}
