package com.vozh.art.dataservice.advice;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerCtrl {


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException: {}", e.getMessage());
        return ResponseEntity.badRequest().body("RuntimeException: " + e.getMessage());
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
        log.error("NullPointerException: {}", e.getMessage());
        return ResponseEntity.badRequest().body("NullPointerException: " + e.getMessage());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("EntityNotFoundException: {}", e.getMessage());
        return ResponseEntity.badRequest().body("EntityNotFoundException: " + e.getMessage());
    }
}
