package com.vozh.art.dataservice.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerCtrl {


    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException e) {
        System.out.println("RuntimeException: " + e.getMessage());
    }
    @ExceptionHandler(NullPointerException.class)
    public void handleNullPointerException(NullPointerException e) {
        System.out.println("NullPointerException: " + e.getMessage());
    }
}
