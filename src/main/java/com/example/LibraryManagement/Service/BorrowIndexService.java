package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BorrowIndexService {
    List<BorrowIndex> getAllBorrowIndex();
    List<Student> getAllStudents();
    List<BookCondition> getAllBookConditionsAdd();
    List<Book> getAllBooks();
    BorrowIndex getBorrowIndexById(int id);
    List<BookCondition> getAllBookConditionsComplete(int id);
    BorrowFine getBorrowFineByBorrowIndex(BorrowIndex borrowIndex);
    List<BorrowIndex> findCurrentBorrowIndex(HttpServletRequest request);
    List<BorrowIndex> findBorrowIndexHistory(HttpServletRequest request);
    List<BorrowIndex> findBorrowIndexNearEstimateTime();
    List<BorrowIndex> findBorrowIndexNearEstimateTimeByStudent(String studentEmail);
    List<Book> getAllBooksActive();
    Book updateBookAfterCreateBorrowIndex(Book book);
}
