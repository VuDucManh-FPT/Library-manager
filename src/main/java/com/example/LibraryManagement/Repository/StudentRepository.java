package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findByStudentEmail(String email);
}
