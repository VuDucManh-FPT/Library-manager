package com.example.LibraryManagement.Security;

import com.example.LibraryManagement.Model.Admin;
import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.AdminRepository;
import com.example.LibraryManagement.Repository.StaffRepository;
import com.example.LibraryManagement.Repository.StudentRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final AdminRepository adminRepository;

    public UserDetailsService(StudentRepository studentRepository, StaffRepository staffRepository,AdminRepository adminRepository) {
        this.studentRepository = studentRepository;
        this.staffRepository = staffRepository;
        this.adminRepository = adminRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Student> studentOpt = studentRepository.findByStudentEmail(email);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            GrantedAuthority authority = new SimpleGrantedAuthority("STUDENT");
            return new User(email, student.getPassword(), Collections.singleton(authority));
        }

        // Tìm kiếm trong bảng Staff
        Optional<Staff> staffOpt = staffRepository.findByStaffEmail(email);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            GrantedAuthority authority = new SimpleGrantedAuthority("STAFF");
            return new User(email, staff.getStaffPassword(), Collections.singleton(authority));
        }
        Optional<Admin> adminOptional = adminRepository.findAdminByEmail(email);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            GrantedAuthority authority = new SimpleGrantedAuthority("ADMIN");
            return new User(email, admin.getPassword(), Collections.singleton(authority));
        }

        // Ném ra ngoại lệ
        throw new UsernameNotFoundException("User not exist");
    }
}
