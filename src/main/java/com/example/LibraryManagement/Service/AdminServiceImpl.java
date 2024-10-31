package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.Admin;
import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdminServiceImpl implements AdminService {

    @Override
    public Admin addAdmin(Admin admin) {
        return null;
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        return null;
    }

    @Override
    public Admin findByAdminEmail(String email) {
        return null;
    }

    @Override
    public List<Student> getAllStudents() {
        return List.of();
    }

    @Override
    public List<Staff> getAllStaffs() {
        return List.of();
    }


}
