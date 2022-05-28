package com.mobilishop.api.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiException {
  private final String message;
  private final HttpStatus httpStatus;
  private final LocalDateTime timestamp;

  public ApiException(String message, HttpStatus httpStatus, LocalDateTime timestamp) {
    this.message = message;
    this.httpStatus = httpStatus;
    this.timestamp = timestamp;
  }
}
