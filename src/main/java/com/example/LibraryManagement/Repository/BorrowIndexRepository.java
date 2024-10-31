package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.BorrowIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowIndexRepository extends JpaRepository<BorrowIndex, Integer> {
    @Query("SELECT bi FROM BorrowIndex bi " +
            "JOIN bi.student s " +
            "JOIN bi.book b " +
            "JOIN bi.conditionAfter bc_a " +
            "JOIN bi.conditionBefore bc_b")
    List<BorrowIndex> findAllBorrowIndex();
}
