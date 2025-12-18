package com.aivle.spring.web.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
