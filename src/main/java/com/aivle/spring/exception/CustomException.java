package com.aivle.spring.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private final HttpStatus status;

    // 기본 형태
    public CustomException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

    // status를 지정하지 않은 경우 기본값 - 400 BAD_REQUEST
    public CustomException(String message){
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
