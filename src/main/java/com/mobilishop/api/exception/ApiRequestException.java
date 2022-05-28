package com.mobilishop.api.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiRequestException extends RuntimeException {
  private HttpStatus httpStatus;

  public ApiRequestException(String message) {
    super(message);
  }

  public ApiRequestException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public ApiRequestException(String message, HttpStatus httpStatus, Throwable cause) {
    super(message, cause);
    this.httpStatus = httpStatus;
  }
}
