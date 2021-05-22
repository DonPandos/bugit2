package com.study.bugit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> baseException(BaseException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", ex.getStatus().value());
        body.put("message", ex.getErrorMessage());

        return new ResponseEntity<>(body, ex.getStatus());
    }


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> customException(CustomException ex)
    {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", ex.getStatus().value());
        body.put("message", ex.getErrorMessage());

        return new ResponseEntity<>(body, ex.getStatus());
    }
}
