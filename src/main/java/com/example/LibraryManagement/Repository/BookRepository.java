package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query(value = "SELECT TOP 5 * FROM Book", nativeQuery = true)
    List<Book> findTop5();
}
