package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Model.BookImport;
import com.example.LibraryManagement.Model.ImportDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportDetailRepository extends JpaRepository<ImportDetail, Integer> {
    ImportDetail findByBook(Book book);
}
