package com.github.daianaegermichels.healthsystem.api.handler;

import com.github.daianaegermichels.healthsystem.dto.ErrorResponse;
import com.github.daianaegermichels.healthsystem.service.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class EntityNotFoundHandler {
    @ExceptionHandler({EntityNotFoundException.class })
    public ResponseEntity<ErrorResponse> entityNotFoundException(EntityNotFoundException e) {

        ErrorResponse error = new ErrorResponse();
        error.setCode(HttpStatus.NOT_FOUND.value());
        error.setTimestamp(LocalDateTime.now());
        error.getMessages().add(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
