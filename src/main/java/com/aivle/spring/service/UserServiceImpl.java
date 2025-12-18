package com.aivle.spring.service;

import com.aivle.spring.domain.Book;
import com.aivle.spring.domain.Category;
import com.aivle.spring.domain.User;
import com.aivle.spring.exception.CustomException;
import com.aivle.spring.jwt.JwtUtil;
import com.aivle.spring.repository.BookRepository;
import com.aivle.spring.repository.LikeRepository;
import com.aivle.spring.repository.UserRepository;
import com.aivle.spring.web.dto.book.BookDetailResponseDto;
import com.aivle.spring.web.dto.book.BookResponseDto;
import com.aivle.spring.web.dto.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService{

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LikeRepository likeRepository;
    private final JwtUtil jwtUtil;


    @Override
    public UserResponseDto signUp(UserRequestDto dto) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomException("이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT);
        }

        // 유저 생성
        // 유저 생성
        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword())) // ️ bcrypt 적용은 나중에
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .build();

        User saved = userRepository.save(user);

        return UserResponseDto.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .name(saved.getName())
                .phoneNumber(saved.getPhoneNumber())
                .build();
    }

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto dto) {
        // 이메일로 유저 조회
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new CustomException("가입되지 않은 이메일입니다."));

        //비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        // Jwt 생성
        String token = jwtUtil.createToken(user.getId(), user.getEmail());

        return UserLoginResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .token(token)
                .build();
    }

    @Override
    public UserResponseDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("유저를 찾을 수 없습니다."));

        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDetailResponseDto> getUserBooks(Long userId) {

        List<Book> books = bookRepository.findByUserId(userId);

        return books.stream()
                .map(book -> {
                    long likeCount = likeRepository.countByBookId(book.getId());

                    // 본인이 자기 책 목록 볼 때: 자기 책에 좋아요 눌렀는지 여부
                    Boolean likedByMe = likeRepository.existsByBookIdAndUserId(book.getId(), userId);

                    return BookDetailResponseDto.builder()
                            .id(book.getId())
                            .title(book.getTitle())
                            .content(book.getContent())
                            .coverUrl(book.getCoverUrl())
                            .category(book.getCategory())
                            .userName(book.getUser().getName())
                            .userId(book.getUser().getId())
                            .likeCount(likeCount)
                            .likedByMe(likedByMe)
                            .build();
                })
                .toList();
    }
}