package com.example.taskmanager.controller;

import com.example.taskmanager.dto.ErrorRs;
import com.example.taskmanager.exception.AlreadyExistsException;

import com.example.taskmanager.exception.RefreshTokenException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ErrorRs> refreshTokenExceptionHandler(RefreshTokenException ex) {
        return buildRs(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorRs> alreadyExistHandler(AlreadyExistsException ex) {
        return buildRs(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorRs> notFoundHandler(EntityNotFoundException ex) {
        return buildRs(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorRs> usernameNotFoundHandler(UsernameNotFoundException ex) {
        return buildRs(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRs> notValidHandler(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errorMessages = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        String errorMessage = String.join("; ", errorMessages);


        return buildRs(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRs> httpMessageNotReadableHandler(HttpMessageNotReadableException ex) {
        return buildRs(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRs> illegalArgumentHandler(IllegalArgumentException ex) {
        return buildRs(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<ErrorRs> buildRs(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus)
                .body(ErrorRs.builder()
                        .message(message)
                        .build());
    }
}
