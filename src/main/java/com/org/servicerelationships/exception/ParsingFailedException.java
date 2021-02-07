package com.org.servicerelationships.exception;

public class ParsingFailedException extends Exception {

  private static final long serialVersionUID = 1L;

  public ParsingFailedException(final String error) {
    super(error);
  }
}
