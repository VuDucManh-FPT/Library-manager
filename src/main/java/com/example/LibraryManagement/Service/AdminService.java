package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.Admin;
import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Model.Student;

import java.util.List;

public interface AdminService {
    Admin addAdmin(Admin admin);
    Admin updateAdmin(Admin admin);
    Admin findByAdminEmail(String email);
    List<Student> getAllStudents();
    List<Staff> getAllStaffs();
}
