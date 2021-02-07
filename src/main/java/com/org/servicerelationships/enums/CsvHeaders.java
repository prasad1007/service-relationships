package com.org.servicerelationships.enums;

public enum CsvHeaders {
  PARENT("Parent"),
  CHILD("Child"),
  LABEL("Label"),
  IMPACT("Impact");

  private final String value;

  CsvHeaders(final String value) {
    this.value = value;
  }
}
