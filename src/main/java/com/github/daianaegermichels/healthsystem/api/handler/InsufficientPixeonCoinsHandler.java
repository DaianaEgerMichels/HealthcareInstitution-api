package com.github.daianaegermichels.healthsystem.api.handler;

import com.github.daianaegermichels.healthsystem.dto.ErrorResponse;
import com.github.daianaegermichels.healthsystem.service.exception.InsufficientPixeonCoinsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class InsufficientPixeonCoinsHandler {
    @ExceptionHandler({InsufficientPixeonCoinsException.class })
    public ResponseEntity<ErrorResponse> insufficientPixeonCoinsException(InsufficientPixeonCoinsException e) {

        ErrorResponse error = new ErrorResponse();
        error.setCode(HttpStatus.PAYMENT_REQUIRED.value());
        error.setTimestamp(LocalDateTime.now());
        error.getMessages().add(e.getMessage());

        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(error);
    }
}
