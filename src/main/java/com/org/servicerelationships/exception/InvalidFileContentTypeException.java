package com.org.servicerelationships.exception;

public class InvalidFileContentTypeException extends Exception {

  private static final long serialVersionUID = 1L;

  public InvalidFileContentTypeException(final String error) {
    super(error);
  }
}
