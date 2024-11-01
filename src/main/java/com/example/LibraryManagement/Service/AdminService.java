package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.Role;
import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Model.Student;

import java.util.List;

public interface AdminService {
    List<Student> getAllStudents();
    List<Staff> getAllStaffs();
    List<Role> getAllRoles();
    String saveStaff(Staff staff);
    Staff findStaffById(int staffId);
    Staff deleteStaffById(int staffId);
    Staff updateStaff(int staffId, Staff staff);
    String saveStudent(Student student);
}
