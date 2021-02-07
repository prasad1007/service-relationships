package com.org.servicerelationships.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.org.servicerelationships.controller.ServiceRelationshipsController;

@RestControllerAdvice
public class ServiceRelationshipsExceptionHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ServiceRelationshipsController.class);

  @ExceptionHandler({InvalidFileContentTypeException.class})
  @ResponseStatus(BAD_REQUEST)
  public void handleExceptionForBadRequest(final Exception e) {
    LOGGER.error("InvalidFileContentTypeException happened : {}", e.getMessage());
  }

  @ExceptionHandler({NumberFormatException.class})
  @ResponseStatus(BAD_REQUEST)
  public void handleNumberFormatExceptionForBadRequest(final Exception e) {
    LOGGER.error("NumberFormatException happened : {}", e.getMessage());
  }

  @ExceptionHandler({ParsingFailedException.class})
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  public void handleParsingFailedException(final Exception e) {
    LOGGER.error("ParsingFailedException happened : {}", e.getMessage());
  }

  @ExceptionHandler({DataNotFoundException.class})
  @ResponseStatus(NOT_FOUND)
  public void handleDataNotFoundException(final Exception e) {
    LOGGER.info("DataNotFoundException happened : {}", e.getMessage());
  }
}
