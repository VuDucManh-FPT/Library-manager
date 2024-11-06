package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.BookImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookImportRepository extends JpaRepository<BookImport, Integer> {
}
