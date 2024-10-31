package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.BorrowIndex;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.BorrowIndexRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowIndexServiceImpl implements BorrowIndexService{
    @Autowired
    private BorrowIndexRepository borrowIndexRepository;
    @Autowired
    private ProfileService profileService;

    public List<BorrowIndex> getAllBorrowIndex() {
        return borrowIndexRepository.findAllBorrowIndex();
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
