package com.aivle.spring.web.controller;

import com.aivle.spring.common.ApiResponse;
import com.aivle.spring.jwt.JwtUtil;
import com.aivle.spring.service.BookService;
import com.aivle.spring.web.dto.book.BookDetailResponseDto;
import com.aivle.spring.web.dto.book.BookListItemDto;
import com.aivle.spring.web.dto.book.BookRequestDto;
import com.aivle.spring.web.dto.book.BookResponseDto;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final JwtUtil jwtUtil;
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponseDto>> createBook(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody BookRequestDto request
            ){

        // "Bearer <token>" → token 분리
        String token = authHeader.replace("Bearer ", "");

        // JWT에서 userId 추출
        Claims claims = jwtUtil.getClaims(token);
        Long userId = Long.valueOf(claims.getSubject());


        BookResponseDto result = bookService.createBook(request, userId);

        return ResponseEntity.ok(ApiResponse.success("도서 등록 성공", result));

    }

    @PutMapping("/{bookId}")
    public ResponseEntity<ApiResponse<BookResponseDto>> updateBook(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody BookRequestDto request,
            @PathVariable Long bookId
    ){
        // "Bearer <token>" → token 분리
        String token = authHeader.replace("Bearer ", "");
        Claims claims = jwtUtil.getClaims(token);
        Long userId = Long.valueOf(claims.getSubject());

        BookResponseDto result=  bookService.updateBook(bookId, request, userId );

        return ResponseEntity.ok(ApiResponse.success("도서 수정 성공", result));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<ApiResponse<?>> deleteBook(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long bookId
    ){
        String token = authHeader.replace("Bearer ", "");
        Claims claims = jwtUtil.getClaims(token);
        Long userId = Long.valueOf(claims.getSubject());

        bookService.deleteBook(bookId, userId);

        return ResponseEntity.ok(ApiResponse.success("도서 삭제 성공", null));

    }

    @GetMapping("/{bookId}")
    public ResponseEntity<ApiResponse<BookDetailResponseDto>> getBookDetail(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long bookId
    ){
        Long userId = null;

        // JWT 있으면 파싱
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            Claims claims = jwtUtil.getClaims(token);
            userId = Long.valueOf(claims.getSubject());
        }

        BookDetailResponseDto detail = bookService.getBookDetail(bookId, userId);

        return ResponseEntity.ok(ApiResponse.success("도서 상세 조회 성공", detail));

    }

    // 좋아요 등록
    @PostMapping("/{bookId}/likes")
    public ResponseEntity<ApiResponse<?>> likeBook(
            @PathVariable Long bookId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = Long.valueOf(jwtUtil.getClaims(token).getSubject());

        bookService.likeBook(bookId, userId);

        return ResponseEntity.ok(ApiResponse.success("좋아요 등록 성공", null));
    }

    // 좋아요 취소
    @DeleteMapping("/{bookId}/likes")
    public ResponseEntity<ApiResponse<?>> unlikeBook(
            @PathVariable Long bookId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = Long.valueOf(jwtUtil.getClaims(token).getSubject());

        bookService.unlikeBook(bookId, userId);

        return ResponseEntity.ok(ApiResponse.success("좋아요 취소 성공", null));
    }

    // 메인페이지 좋아요 기준 목록 조회
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<BookListItemDto>>> getPopularBooks(){

        List<BookListItemDto> result = bookService.getPopularBooks();

        return ResponseEntity.ok(
                ApiResponse.success("인기 도서 조회 성공", result)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookListItemDto>>> getBooks(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "latest") String sort
    ) {
        List<BookListItemDto> list = bookService.getBookList(category, sort);
        return ResponseEntity.ok(ApiResponse.success("도서 목록 조회 성공", list));
    }
}
