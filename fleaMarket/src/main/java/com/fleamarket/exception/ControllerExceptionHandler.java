package com.fleamarket.exception;

import com.fleamarket.model.dto.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

  @ExceptionHandler({MissingServletRequestParameterException.class, IllegalArgumentException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseError handleIllegalArgumentException(Exception ex, WebRequest request) {
    String errorMessage = "Error 400: Bad request - " + ex.getMessage();
    log.error(errorMessage);
    return new ResponseError(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler({ProductTakenException.class, SellerTakenException.class})
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseError handleResponseException(Exception ex, WebRequest request) {
    String errorMessage = "Error 409: Conflict - " + ex.getMessage();
    log.error(errorMessage);
    return new ResponseError(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ResponseError handleMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex, WebRequest request) {
    String errorMessage = "Error 405: Method not supported - " + ex.getMessage();
    log.error(errorMessage);
    return new ResponseError(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
  }

  @ExceptionHandler({RuntimeException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseError handleAllExceptions(RuntimeException ex, WebRequest request) {
    String errorMessage = "Error 500: Internal server error - " + ex.getMessage();
    log.error(errorMessage);
    return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }

  @ExceptionHandler({SellerNotFoundException.class, ProductNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseError notFoundException(RuntimeException ex, WebRequest request) {
    String errorMessage = "Error 404: Not Found - " + ex.getMessage();
    log.error(errorMessage);
    return new ResponseError(HttpStatus.NOT_FOUND, ex.getMessage());
  }
}
