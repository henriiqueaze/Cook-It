package com.p5Project.cookIt.exceptions.handler;

import com.p5Project.cookIt.exceptions.ResourceNotFoundException;
import com.p5Project.cookIt.exceptions.model.RestErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RestErrorMessage> resourceNotFoundHandler(ResourceNotFoundException ex, HttpServletRequest request) {
        RestErrorMessage error = RestErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .errorCode("RESOURCE_NOT_FOUND")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .traceId(request.getHeader("X-Trace-Id"))
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
