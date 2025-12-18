package com.aivle.spring.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 도서 식별 아이디

    @Column(nullable = false)
    private String title; // 도서 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 책 내용

    @Column(nullable = false, columnDefinition = "TEXT")
    private String coverUrl; // 책 표지 URL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category; // 도서 카테고리

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
