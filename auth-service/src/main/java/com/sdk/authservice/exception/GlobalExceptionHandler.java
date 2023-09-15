package com.sdk.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sdk.authservice.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ApiResponse<Void> errorResponse = new ApiResponse<>(HttpStatus.CONFLICT.value(), ex.getMessage());
        /* return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT); */
         return ResponseEntity.status(HttpStatus.CONFLICT)
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(errorResponse);
    }

    @ExceptionHandler(MissingFieldException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingFieldException(MissingFieldException ex) {
        ApiResponse<Void> errorResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UserNotFoundException ex) {
        ApiResponse<Void> errorResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(errorResponse);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        ApiResponse<Void> errorResponse = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(errorResponse);
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidJwtAuthenticationException(InvalidJwtAuthenticationException ex) {
        ApiResponse<Void> errorResponse = new ApiResponse<>(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(errorResponse);
    }

}


