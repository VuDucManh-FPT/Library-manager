package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.BorrowIndex;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BorrowIndexService {
    List<BorrowIndex> getAllBorrowIndex();
    List<BorrowIndex> findCurrentBorrowIndex(HttpServletRequest request);
    List<BorrowIndex> findBorrowIndexHistory(HttpServletRequest request);
}
