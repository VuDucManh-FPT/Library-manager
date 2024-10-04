package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.Student;

public interface StudentService {
    Student addStudent(Student student);
    Student updateStudent(Student student);
    Student findStudentByStudentEmail(String studentEmail);
}
