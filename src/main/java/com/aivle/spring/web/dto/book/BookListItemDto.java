package com.aivle.spring.web.dto.book;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookListItemDto {
    private Long bookId;
    private String title;
    private String author;
    private Long likeCount;
    private String coverUrl;
}
