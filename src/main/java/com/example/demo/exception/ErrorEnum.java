package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorEnum {

    WRONG_PASSWORD(400,"Wrong password",HttpStatus.UNAUTHORIZED),
    USERNAME_ALREADY_EXISTS(409,"Username already exists",HttpStatus.BAD_REQUEST),
    Email_ALREADY_EXISTS(409,"Email already exists",HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(400,"User not found",HttpStatus.NOT_FOUND),
    INVALID_TOKEN(403,"Invalid token",HttpStatus.FORBIDDEN),
    TASK_NOT_FOUND(400,"Task not found",HttpStatus.NOT_FOUND),
    ;

    private int code;
    private String message;
    private HttpStatus httpStatus;
}
