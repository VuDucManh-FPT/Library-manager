package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Optional<Staff> findByStaffEmail(String email);
    boolean existsByStaffEmail(String staffEmail);
    boolean existsStaffByRoleRoleName(String roleName);
}
