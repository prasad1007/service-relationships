package com.org.servicerelationships.domain.rest;

public class FileLocationRequest {

  private String fileLocation;

  public FileLocationRequest(final String fileLocation) {
    this.fileLocation = fileLocation;
  }

  public FileLocationRequest() {}

  public String getFileLocation() {
    return fileLocation;
  }
}
