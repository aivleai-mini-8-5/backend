package com.aivle.spring.service;

import com.aivle.spring.web.dto.book.BookDetailResponseDto;
import com.aivle.spring.web.dto.book.BookListItemDto;
import com.aivle.spring.web.dto.book.BookRequestDto;
import com.aivle.spring.web.dto.book.BookResponseDto;

import java.util.List;

public interface BookService {
    BookResponseDto createBook(BookRequestDto dto, Long userId);

    BookResponseDto updateBook(Long bookId, BookRequestDto dto, Long userId);

    void deleteBook(Long bookId, Long userId);

    BookDetailResponseDto getBookDetail(Long bookId, Long userId);

    void likeBook(Long bookId, Long userId);

    void unlikeBook(Long bookId, Long userId);

    public List<BookResponseDto> getMyBooks(Long userId);

    List<BookListItemDto> getPopularBooks();

    List<BookListItemDto> getBookList(String category, String sort);

}
