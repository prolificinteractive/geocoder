package com.prolificinteractive.geocoder;

public class RetriableException extends Exception {
  public RetriableException(final String message) {
    super(message);
  }

  public RetriableException(final Throwable cause) {
    super(cause);
  }
}
