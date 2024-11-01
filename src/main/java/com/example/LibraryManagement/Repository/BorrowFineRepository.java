package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.BorrowFine;
import com.example.LibraryManagement.Model.BorrowIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowFineRepository extends JpaRepository<BorrowFine,Integer> {
    BorrowFine getBorrowFineByBorrowIndex(BorrowIndex borrowIndex);
}
