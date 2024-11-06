package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.Book;
import com.example.LibraryManagement.Model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query(value = "SELECT TOP 12 * FROM Book", nativeQuery = true)
    List<Book> findTop12();

    List<Book> findByBookNameContaining(String bookName);

    @Query("SELECT b FROM Book b WHERE b.bookId <> :bookId AND EXISTS (SELECT c FROM b.genres c WHERE c IN :genres) ORDER BY b.bookId DESC")
    List<Book> findTopByGenres(@Param("genres") Set<Genre> genres, @Param("bookId") Integer bookId, Pageable pageable);
    boolean existsByBookName(String name);

}
