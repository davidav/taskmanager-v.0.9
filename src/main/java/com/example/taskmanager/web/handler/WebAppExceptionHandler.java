package com.example.taskmanager.web.handler;

import com.example.taskmanager.exception.AlreadyExistsException;
import com.example.taskmanager.exception.EntityNotFoundException;
import com.example.taskmanager.exception.RefreshTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class WebAppExceptionHandler {

    @ExceptionHandler(value = RefreshTokenException.class)
    public ResponseEntity<ErrorResponseBody> refreshTokenExceptionHandler(RefreshTokenException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.FORBIDDEN, ex, webRequest);
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<ErrorResponseBody> alreadyExistHandler(AlreadyExistsException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex, webRequest);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> notFoundHandler(EntityNotFoundException ex, WebRequest webRequest) {
        return buildResponse(HttpStatus.NOT_FOUND, ex, webRequest);
    }

    private ResponseEntity<ErrorResponseBody> buildResponse(HttpStatus httpStatus, Exception ex, WebRequest webRequest) {
        return ResponseEntity.status(httpStatus)
                .body(ErrorResponseBody.builder()
                        .message(ex.getMessage())
                        .description(webRequest.getDescription(false))
                        .build());
    }
}
