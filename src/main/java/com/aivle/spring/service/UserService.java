package com.aivle.spring.service;

import com.aivle.spring.web.dto.book.BookDetailResponseDto;
import com.aivle.spring.web.dto.book.BookResponseDto;
import com.aivle.spring.web.dto.user.*;

import java.util.List;

public interface UserService {
    UserResponseDto signUp(UserRequestDto dto);
    UserLoginResponseDto login(UserLoginRequestDto dto);

    UserResponseDto getUserInfo(Long userId);

    // 마이페이지 → 유저 도서 목록 조회
    List<BookDetailResponseDto> getUserBooks(Long userId);
}
