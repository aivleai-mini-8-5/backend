package com.aivle.spring.service;

import com.aivle.spring.domain.Book;
import com.aivle.spring.domain.Category;
import com.aivle.spring.domain.Like;
import com.aivle.spring.domain.User;
import com.aivle.spring.exception.CustomException;
import com.aivle.spring.repository.BookRepository;
import com.aivle.spring.repository.LikeRepository;
import com.aivle.spring.repository.UserRepository;
import com.aivle.spring.web.dto.book.BookDetailResponseDto;
import com.aivle.spring.web.dto.book.BookListItemDto;
import com.aivle.spring.web.dto.book.BookRequestDto;
import com.aivle.spring.web.dto.book.BookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LikeRepository likeRepository;

    @Transactional
    @Override
    public BookResponseDto createBook(BookRequestDto dto, Long userId) {
        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // Book 객체 생성
        Book book = Book.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .coverUrl(dto.getCoverUrl())
                .category(dto.getCategory())
                .user(user)
                .build();

        // DB에 저장
        Book saved = bookRepository.save(book);

        // 반환

        return BookResponseDto.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .content(saved.getContent())
                .coverUrl(saved.getCoverUrl())
                .category(saved.getCategory())
                .userId(userId)
                .build();
    }

    @Transactional
    @Override
    public BookResponseDto updateBook(Long bookId, BookRequestDto dto, Long userId) {
        // 책 조회
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException("존재하지 않은 도서입니다.", HttpStatus.NOT_FOUND));

        // 본인 책인지 검증
        if(!userId.equals(book.getUser().getId())) throw new CustomException("도서에 대한 수정권한이 없습니다.", HttpStatus.UNAUTHORIZED);

        // 수정
        book = Book.builder()
                .id(book.getId())
                .user(book.getUser())
                .title(dto.getTitle())
                .content(dto.getContent())
                .coverUrl(dto.getCoverUrl())
                .category(dto.getCategory())
                .createdAt(book.getCreatedAt())
                .build();

        // 수정된 도서로 저장
        Book saved = bookRepository.save(book);


        return BookResponseDto.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .content(saved.getContent())
                .coverUrl(saved.getCoverUrl())
                .category(saved.getCategory())
                .userId(userId)
                .build();
    }

    @Transactional
    @Override
    public void deleteBook(Long bookId, Long userId) {
        // 책 조회
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException("존재하지 않은 도서입니다.", HttpStatus.NOT_FOUND));

        // 본인 책인지 검증
        if(!userId.equals(book.getUser().getId())) throw new CustomException("도서에 대한 수정권한이 없습니다.", HttpStatus.UNAUTHORIZED);

        // 좋아요 먼저 삭제
        likeRepository.deleteByBookId(bookId);

        bookRepository.delete(book);
    }

    @Transactional(readOnly = true)
    @Override
    public BookDetailResponseDto getBookDetail(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException("도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        long likeCount = likeRepository.countByBookId(bookId);

        // 내가 좋아요 눌렀는지
        Boolean likedByMe = null;
        if (userId != null) {  // 로그인 안 한 상태면 null로 처리
            likedByMe = likeRepository.existsByBookIdAndUserId(bookId, userId);
        }

        return BookDetailResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .content(book.getContent())
                .coverUrl(book.getCoverUrl())
                .category(book.getCategory())
                .userName(book.getUser().getName())
                .userId(book.getUser().getId())
                .createdAt(book.getCreatedAt().format(FORMATTER))
                .updatedAt(book.getUpdatedAt().format(FORMATTER))
                .likeCount(likeCount)
                .likedByMe(likedByMe)
                .build();
    }

    @Override
    @Transactional
    public void likeBook(Long bookId, Long userId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException("도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // 이미 좋아요 했다면 막기
        if (likeRepository.existsByBookIdAndUserId(bookId, userId)) {
            throw new CustomException("이미 좋아요한 도서입니다.", HttpStatus.CONFLICT);
        }

        Like like = Like.builder()
                .book(book)
                .user(user)
                .build();

        likeRepository.save(like);
    }


    @Override
    @Transactional
    public void unlikeBook(Long bookId, Long userId) {

        Like like = likeRepository.findByBookIdAndUserId(bookId, userId)
                .orElseThrow(() -> new CustomException("좋아요 정보가 없습니다.", HttpStatus.NOT_FOUND));

        likeRepository.delete(like);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> getMyBooks(Long userId) {

        List<Book> books = bookRepository.findByUserId(userId);

        return books.stream()
                .map(book -> BookResponseDto.builder()
                        .id(book.getId())
                        .title(book.getTitle())
                        .content(book.getContent())
                        .coverUrl(book.getCoverUrl())
                        .category(book.getCategory())
                        .userId(book.getUser().getId())
                        .build())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookListItemDto> getPopularBooks() {

        // 상위 10개 조회
        List<Book> books = bookRepository.findTopPopularBooks(PageRequest.of(0, 10));

        // DTO 변환
        return books.stream()
                .map(book -> BookListItemDto.builder()
                        .bookId(book.getId())
                        .title(book.getTitle())
                        .author(book.getUser().getName())
                        .coverUrl(book.getCoverUrl())
                        .likeCount(likeRepository.countByBookId(book.getId()))
                        .build()
                )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookListItemDto> getBookList(String category, String sort) {

        Category categoryEnum = null;

        // category가 있으면 enum으로 변환
        if (category != null && !category.isBlank()) {
            try {
                categoryEnum = Category.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new CustomException("잘못된 카테고리입니다.", HttpStatus.BAD_REQUEST);
            }
        }

        // 정렬 기준에 따라 분기
        List<Book> books;
        if ("popular".equalsIgnoreCase(sort)) {
            books = bookRepository.findAllByCategoryOrderByPopular(categoryEnum);
        } else {
            // 기본 최신순
            books = bookRepository.findAllByCategoryOrderByLatest(categoryEnum);
        }

        // DTO 매핑
        return books.stream()
                .map(book -> BookListItemDto.builder()
                        .bookId(book.getId())
                        .title(book.getTitle())
                        .author(book.getUser().getName())
                        .coverUrl(book.getCoverUrl())
                        .likeCount(likeRepository.countByBookId(book.getId()))
                        .build()
                )
                .toList();
    }


    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

}
