package com.avanade.ebr.handlers;

import com.avanade.ebr.dto.ErrorMessage;
import com.avanade.ebr.exceptions.InvalidInputException;
import com.avanade.ebr.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public final class RestExceptionHandler {

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorMessage> invalidInputHandler(Exception exception, WebRequest webRequest) {
        return handle(exception, HttpStatus.BAD_REQUEST, webRequest.getDescription(false));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFoundHandler(Exception exception, WebRequest webRequest) {
        return handle(exception, HttpStatus.NOT_FOUND, webRequest.getDescription(false));
    }

    private ResponseEntity<ErrorMessage> handle(Exception exception, HttpStatus httpStatus, String description) {
        exception.printStackTrace();
        return new ResponseEntity<>(new ErrorMessage(httpStatus, LocalDateTime.now(), exception.getMessage(), description), httpStatus);
    }
}
