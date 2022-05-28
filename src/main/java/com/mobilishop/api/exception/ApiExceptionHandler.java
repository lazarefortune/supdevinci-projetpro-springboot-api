package com.mobilishop.api.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException ex) {
        if ( ex.getHttpStatus() == null ) {
            ex.setHttpStatus(HttpStatus.BAD_REQUEST);
        }

        ApiException apiException = new ApiException(
                ex.getMessage(),
                ex.getHttpStatus(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, ex.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        //return super.handleNoHandlerFoundException(ex, headers, status, request);
        return new ResponseEntity<>(new ApiException(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        ), HttpStatus.NOT_FOUND);
    }
}
