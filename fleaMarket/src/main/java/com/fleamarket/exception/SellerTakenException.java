package com.fleamarket.exception;

public class SellerTakenException extends RuntimeException {
  public SellerTakenException(String message) {
    super(message);
  }
}
