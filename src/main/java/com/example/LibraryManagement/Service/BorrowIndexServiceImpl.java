package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.*;
import com.example.LibraryManagement.Repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BorrowIndexServiceImpl implements BorrowIndexService{
    private BorrowIndexRepository borrowIndexRepository;
    private StudentRepository studentRepository;
    private BookConditionRepository bookConditionRepository;
    private BookRepository bookRepository;
    private BorrowFineRepository fineRepository;

    public List<BorrowIndex> getAllBorrowIndex() {
        return borrowIndexRepository.findAllBorrowIndex();
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<BookCondition> getAllBookConditionsAdd() {
        return bookConditionRepository.findAllByBookConditionIdNot(6);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public BorrowIndex getBorrowIndexById(int id) {
        return borrowIndexRepository.findById(id).get();
    }

    @Override
    public List<BookCondition> getAllBookConditionsComplete(int id) {
        return bookConditionRepository.findAllByBookConditionIdGreaterThanEqual(id);
    }

    @Override
    public BorrowFine getBorrowFineByBorrowIndex(BorrowIndex borrowIndex) {
        return fineRepository.getBorrowFineByBorrowIndex(borrowIndex);
    }

}
