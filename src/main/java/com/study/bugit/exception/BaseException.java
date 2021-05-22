package com.study.bugit.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BaseException extends RuntimeException {
    private HttpStatus status;
    private String errorMessage;

    public BaseException() {
        super();
    }

    public BaseException(HttpStatus status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
