package com.aivle.spring.web.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 50)
    private String email;


    @NotBlank
    @Pattern(
            regexp = "^[A-Za-z\\d@$!%*?&#]{8,20}$",
            message = "비밀번호는 영어 대소문자, 숫자, 특수문자(@$!%*?&#)만 허용되며 8~20자로 입력해주세요."
    )
    private String password;

    @NotBlank
    @Pattern(
            regexp = "^[가-힣]+$",
            message = "이름은 한글만 입력할 수 있으며 공백 없이 입력해야 합니다."
    )
    private String name;

    @NotBlank
    @Pattern(
            regexp = "^(01[016789])-?\\d{3,4}-?\\d{4}$",
            message = "전화번호는 010-0000-0000 또는 01000000000 형식으로 입력해주세요."
    )
    private String phoneNumber;
}
