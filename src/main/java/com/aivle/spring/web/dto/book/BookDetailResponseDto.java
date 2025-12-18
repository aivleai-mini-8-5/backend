package com.aivle.spring.web.dto.book;

import com.aivle.spring.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BookDetailResponseDto {

    private Long id;
    private String title;
    private String content;
    private String coverUrl;
    private Category category;
    private String userName;
    private Long userId;
    private String createdAt;
    private String updatedAt;

    private Long likeCount; // 좋아요 수
    private Boolean likedByMe; // 사용자가 좋아요를 눌렀는지 여부 (JWT있을 때만 true/false) - 없이 조회하면 null처리
}
