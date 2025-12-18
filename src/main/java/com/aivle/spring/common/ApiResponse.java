package com.aivle.spring.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ApiResponse <T>{

    private boolean success;    // 요청 성공 여부
    private String message;     // 응답 메시지
    private T data;           // 응답 데이터

    // 성공 응답
    public static <T> ApiResponse<T> success(T result){
        return ApiResponse.<T>builder()
                .success(true)
                .message("성공입니다.")
                .data(result)
                .build();
    }
    // 성공 + 메시지
    public static <T> ApiResponse<T> success(String message, T result) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(result)
                .build();
    }

    // 실패 응답
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }

    // 실패 응답 (데이터 포함)
    public static <T> ApiResponse<T> error(String message, T result) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(result)
                .build();
    }

}
