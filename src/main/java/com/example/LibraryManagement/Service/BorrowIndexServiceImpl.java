package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Model.BookCondition;
import com.example.LibraryManagement.Model.BorrowIndex;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.BookConditionRepository;
import com.example.LibraryManagement.Repository.BookRepository;
import com.example.LibraryManagement.Repository.BorrowIndexRepository;
import com.example.LibraryManagement.Repository.StudentRepository;
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

    public List<BorrowIndex> getAllBorrowIndex() {
        return borrowIndexRepository.findAllBorrowIndex();
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<BookCondition> getAllBookConditions() {
        return bookConditionRepository.findAll();
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public BorrowIndex getBorrowIndexById(int id) {
        return borrowIndexRepository.findById(id).get();
    }

}
