package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.Role;
import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Response.BorrowFineAdminDashResponse;

import java.util.List;

public interface AdminService {
    List<Student> getAllStudents();
    List<Staff> getAllStaffs();
    List<Role> getAllRoles();
    String saveStaff(Staff staff);
    Staff findStaffById(int staffId);
    Staff banStaffById(int staffId);
    Staff updateStaff(Staff staff);
    String saveStudent(Student student);
    Student updateStudent(Student studentId);
    Student findStudentById(int studentId);
    Student banStudentById(int studentId);
    void deleteStudentById(int studentId);
    BorrowFineAdminDashResponse getAllBorrowFineForDashAdmin();
}
