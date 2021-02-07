package com.org.servicerelationships.domain.rest;

public class ServiceRelationshipsRequest {

  private String parent;

  private String child;

  private String label;

  private float impact;

  public ServiceRelationshipsRequest(
      final String parent, final String child, final String label, final float impact) {
    this.parent = parent;
    this.child = child;
    this.label = label;
    this.impact = impact;
  }

  public ServiceRelationshipsRequest() {}

  public String getParent() {
    return parent;
  }

  public String getChild() {
    return child;
  }
}
