package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.*;
import com.example.LibraryManagement.Repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BorrowIndexServiceImpl implements BorrowIndexService{
    private BorrowIndexRepository borrowIndexRepository;
    private StudentRepository studentRepository;
    private BookConditionRepository bookConditionRepository;
    private BookRepository bookRepository;
    private BorrowFineRepository fineRepository;
    @Autowired
    private ProfileService profileService;

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
    @Override
    public List<BorrowIndex> findCurrentBorrowIndex(HttpServletRequest request) {
        String email = profileService.getUserEmailFromCookie(request);
        if (email == null) {
            return new ArrayList<>(); // Không có email trong cookie
        }

        Optional<Object> user = profileService.getUserByEmail(email);
        if (user.isPresent() && user.get() instanceof Student) {
            return borrowIndexRepository.findByStudentAndReturnDateIsNull((Student) user.get());
        }
        return new ArrayList<>();
    }

    @Override
    public List<BorrowIndex> findBorrowIndexHistory(HttpServletRequest request) {
        String email = profileService.getUserEmailFromCookie(request);
        if (email == null) {
            return new ArrayList<>(); // Không có email trong cookie
        }

        Optional<Object> user = profileService.getUserByEmail(email);
        if (user.isPresent() && user.get() instanceof Student) {
            return borrowIndexRepository.findByStudentAndReturnDateIsNotNull((Student) user.get());
        }
        return new ArrayList<>();
    }
}
