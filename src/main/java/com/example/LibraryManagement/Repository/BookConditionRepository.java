package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.BookCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookConditionRepository extends JpaRepository<BookCondition, Integer> {
}
