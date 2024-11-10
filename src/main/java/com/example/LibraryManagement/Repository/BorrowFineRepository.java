package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.BorrowFine;
import com.example.LibraryManagement.Model.BorrowIndex;
import com.example.LibraryManagement.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowFineRepository extends JpaRepository<BorrowFine,Integer> {
    BorrowFine getBorrowFineByBorrowIndex(BorrowIndex borrowIndex);
    BorrowFine findByBorrowIndexStudentAndStatus(Optional<Student> student, String status);

}
