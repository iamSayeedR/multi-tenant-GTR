package com.example.multi_tanent.warehouse.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllException(Exception exception) {
        log.error("Exception : {}", exception.getMessage());
        return ResponseEntity.status(400).body(Map.of("message", "Client Error"));
    }


}
