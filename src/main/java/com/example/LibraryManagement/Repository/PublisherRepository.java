package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
}
