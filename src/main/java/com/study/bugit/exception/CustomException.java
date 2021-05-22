package com.study.bugit.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomException extends BaseException{

    public CustomException() {
        super();
    }

    public CustomException(HttpStatus status, String errorMessage) {
        super(status, errorMessage);
    }
}
