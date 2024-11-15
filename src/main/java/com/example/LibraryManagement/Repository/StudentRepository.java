package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByStudentEmail(String email);
    boolean existsStudentByStudentEmail(String studentEmail);

}
