package com.example.backendtask.common.exception.exceptionhandler;


import com.example.backendtask.common.exception.constant.ErrorCode;
import com.example.backendtask.common.exception.object.ClientException;
import com.example.backendtask.common.exception.object.ServerException;
import com.example.backendtask.common.response.ErrorResponse;
import com.example.backendtask.domain.auth.controller.AuthController;
import com.example.backendtask.domain.user.controller.UserController;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorResponse> handleClientException(ClientException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }


    @ExceptionHandler(ServerException.class)
    public ResponseEntity<com.example.backendtask.common.response.ErrorResponse> handleServerException(ServerException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }
}
