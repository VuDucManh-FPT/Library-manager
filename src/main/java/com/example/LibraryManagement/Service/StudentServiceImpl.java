package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private StudentRepository studentRepository;

    @Override
    public Student addStudent(Student student) {
        return null;
    }

    @Override
    public Student updateStudent(Student student) {
        return null;
    }

    @Override
    public Student findStudentByStudentEmail(String studentEmail) {
        return null;
    }
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByStudentEmail(email);
    }
    public Student getStudentById(int id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student updateProfile(int id, Student updatedStudent) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            student.setStudentEmail(updatedStudent.getStudentEmail());
            student.setStudentName(updatedStudent.getStudentName());
            studentRepository.save(student);
        }
        return student;
    }

    public boolean changePassword(int id, String oldPassword, String newPassword) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null && student.getPassword().equals(oldPassword)) {
            student.setPassword(newPassword);
            studentRepository.save(student);
            return true;
        }
        return false;
    }
}
