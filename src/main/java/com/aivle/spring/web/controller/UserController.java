package com.aivle.spring.web.controller;

import com.aivle.spring.common.ApiResponse;
import com.aivle.spring.jwt.JwtUtil;
import com.aivle.spring.service.BookService;
import com.aivle.spring.service.UserService;
import com.aivle.spring.web.dto.book.BookDetailResponseDto;
import com.aivle.spring.web.dto.book.BookResponseDto;
import com.aivle.spring.web.dto.user.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final BookService bookService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponseDto>> signUp(
            @Valid @RequestBody UserRequestDto request
            ){
        UserResponseDto result = userService.signUp(request);
        return ResponseEntity.ok(ApiResponse.success("회원가입 성공", result));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponseDto>> login(
            @Valid @RequestBody UserLoginRequestDto request
            ){
        UserLoginResponseDto result = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success("로그인 성공", result));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserInfo(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserIdFromToken(token);

        UserResponseDto user = userService.getUserInfo(userId);

        return ResponseEntity.ok(ApiResponse.success("유저 정보 조회 성공", user));
    }

    @GetMapping("/books")
    public ResponseEntity<ApiResponse<List<BookDetailResponseDto>>> getMyBooks(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.getUserIdFromToken(token);

        List<BookDetailResponseDto> books = userService.getUserBooks(userId);

        return ResponseEntity.ok(ApiResponse.success("유저 도서 목록 조회 성공", books));
    }
}
