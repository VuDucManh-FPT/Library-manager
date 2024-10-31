package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Integer> {
}
