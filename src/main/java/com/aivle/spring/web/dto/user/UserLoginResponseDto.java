package com.aivle.spring.web.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserLoginResponseDto {
    private Long userId;
    private String email;
    private String token;  // JWT
}
