package com.aivle.spring.repository;

import com.aivle.spring.domain.Book;
import com.aivle.spring.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIdAndUserId(Long id, Long userId);

    @Query("""
        SELECT b 
        FROM Book b 
        LEFT JOIN Like l ON b.id = l.book.id
        GROUP BY b.id
        ORDER BY COUNT(l.id) DESC
    """)
    List<Book> findTopPopularBooks(Pageable pageable);

    // 최신순 조회 + 카테고리(OPtional)
    @Query("""
    SELECT b 
    FROM Book b
    WHERE (:category IS NULL OR b.category = :category)
    ORDER BY b.createdAt DESC
""")
    List<Book> findAllByCategoryOrderByLatest(@Param("category") Category category);

    @Query("""
    SELECT b
    FROM Book b
    LEFT JOIN Like l ON l.book.id = b.id
    WHERE (:category IS NULL OR b.category = :category)
    GROUP BY b.id
    ORDER BY COUNT(l.id) DESC
""")
    List<Book> findAllByCategoryOrderByPopular(@Param("category") Category category);

    List<Book> findByUserId(Long userId);
}
