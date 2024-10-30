package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.BorrowIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowIndexRepository extends JpaRepository<BorrowIndex, Integer> {
    @Query("SELECT bi FROM BorrowIndex bi " +
            "LEFT JOIN bi.student s " +
            "LEFT JOIN bi.book b " +
            "LEFT JOIN bi.conditionAfter bc_a " +
            "LEFT JOIN bi.conditionBefore bc_b")
    List<BorrowIndex> findAllBorrowIndex();
}
