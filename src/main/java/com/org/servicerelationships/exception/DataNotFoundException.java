package com.org.servicerelationships.exception;

public class DataNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;

  public DataNotFoundException(final String error) {
    super(error);
  }
}
