package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.*;
import com.example.LibraryManagement.Repository.*;
import com.example.LibraryManagement.Security.JwtProvider;
import io.jsonwebtoken.JwtParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BorrowIndexServiceImpl implements BorrowIndexService{
    private BorrowIndexRepository borrowIndexRepository;
    private StudentRepository studentRepository;
    private BookConditionRepository bookConditionRepository;
    private BookRepository bookRepository;
    private BorrowFineRepository fineRepository;
    private final JwtProvider jwtProvider;
    private final ImportDetailRepository importDetailRepository;
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
        String token = jwtProvider.getJwtFromCookies(request);
        String email = jwtProvider.getEmail(token);
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
        String token = jwtProvider.getJwtFromCookies(request);
        String email = jwtProvider.getEmail(token);
        if (email == null) {
            return new ArrayList<>(); // Không có email trong cookie
        }

        Optional<Object> user = profileService.getUserByEmail(email);
        if (user.isPresent() && user.get() instanceof Student) {
            return borrowIndexRepository.findByStudentAndReturnDateIsNotNull((Student) user.get());
        }
        return new ArrayList<>();
    }

    @Override
    public List<BorrowIndex> findBorrowIndexNearEstimateTime() {
        Date now = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, -30);
        Date dayBefore = cal.getTime();

        cal.setTime(now);
        cal.add(Calendar.DATE, 1);
        Date dayAfter = cal.getTime();

        return borrowIndexRepository.findBorrowIndexNearEstimateTime(dayBefore, dayAfter);
    }

    @Override
    public List<BorrowIndex> findBorrowIndexNearEstimateTimeByStudent(String studentEmail) {
        Date now = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, -30);
        Date dayBefore = cal.getTime();

        cal.setTime(now);
        cal.add(Calendar.DATE, 1);
        Date dayAfter = cal.getTime();
        return borrowIndexRepository.findBorrowIndexNearEstimateTimeAndByStudentEmail(dayBefore, dayAfter, studentEmail);

    }

    @Override
    public List<Book> getAllBooksActive() {
        return bookRepository.findByIsActiveIsTrue();
    }

    @Override
    public Book updateBookAfterCreateBorrowIndex(Book book) {
        ImportDetail importDetail = decreaseQuantity(book);
        int quantity = importDetail.getQuantity();
        if (quantity <= 0) {
            book.setActive(false);
        }
        return bookRepository.save(book);
    }
    public ImportDetail decreaseQuantity(Book book) {
        ImportDetail importDetail =  importDetailRepository.findByBook(book);
        importDetail.setQuantity(importDetail.getQuantity()-1);
        importDetailRepository.save(importDetail);
        return importDetail;
    }

}
