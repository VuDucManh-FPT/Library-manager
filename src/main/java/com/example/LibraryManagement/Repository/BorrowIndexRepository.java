package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Model.BorrowIndex;
import com.example.LibraryManagement.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BorrowIndexRepository extends JpaRepository<BorrowIndex, Integer> {
    @Query("SELECT bi FROM BorrowIndex bi " +
            "LEFT JOIN bi.student s " +
            "LEFT JOIN bi.book b " +
            "LEFT JOIN bi.conditionAfter bc_a " +
            "LEFT JOIN bi.conditionBefore bc_b")
    List<BorrowIndex> findAllBorrowIndex();
    List<BorrowIndex> findByStudentAndReturnDateIsNull(Student student);
    List<BorrowIndex> findByStudentAndReturnDateIsNotNull(Student student);
    boolean existsBorrowIndexByBook(Book book);
    List<BorrowIndex> findByStudent(Student student);
    @Query("SELECT b FROM BorrowIndex b WHERE b.estimateDate BETWEEN :startDate AND :endDate AND b.returnDate IS NULL")
    List<BorrowIndex> findBorrowIndexNearEstimateTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    @Query("SELECT b FROM BorrowIndex b WHERE b.estimateDate BETWEEN :startDate AND :endDate AND b.returnDate IS NULL AND b.student.studentEmail = :studentEmail")
    List<BorrowIndex> findBorrowIndexNearEstimateTimeAndByStudentEmail(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("studentEmail") String studentEmail);

}
