package com.fleamarket.exception;

public class ProductTakenException extends RuntimeException {
  public ProductTakenException(String message) {
    super(message);
  }
}
