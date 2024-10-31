package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.Admin;
import com.example.LibraryManagement.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findAdminByEmail(String email);
    List<Admin> findAll();

}
