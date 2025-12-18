package com.aivle.spring.web.dto.book;

import com.aivle.spring.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BookResponseDto {

    private Long id;
    private String title;
    private String content;
    private String coverUrl;
    private Category category;
    private Long userId;

}
