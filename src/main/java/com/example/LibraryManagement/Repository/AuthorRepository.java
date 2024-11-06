package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query(value = "SELECT TOP 12 * FROM Author", nativeQuery = true)
    List<Author> findTop12();
}
