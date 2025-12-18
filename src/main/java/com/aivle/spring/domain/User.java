package com.aivle.spring.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        //유저 식별 id

    @Column(nullable = false, unique = true)
    private String email;       // 유저 이메일(로그인)

    @Column(nullable = false)
    private String password;    // 비밀번호

    @Column(nullable = false)
    private String name;        // 유저 이름

    @Column(nullable = false)
    private String phoneNumber; // 전화번호

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
