package com.example.demo.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler{
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleWrongPassword(CustomException ex){
        ErrorEnum error= ex.getError();
        ErrorResponse response = new ErrorResponse(error.getCode(),error.getMessage(),LocalDateTime.now());
        return ResponseEntity.status(error.getHttpStatus()).body(response);
    }
}
