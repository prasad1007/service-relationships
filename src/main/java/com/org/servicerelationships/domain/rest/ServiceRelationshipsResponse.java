package com.org.servicerelationships.domain.rest;

public class ServiceRelationshipsResponse {

  private String parent;

  private String child;

  private String label;

  private float impact;

  public ServiceRelationshipsResponse(
      final String parent, final String child, final String label, final float impact) {
    this.parent = parent;
    this.child = child;
    this.label = label;
    this.impact = impact;
  }

  public ServiceRelationshipsResponse() {}

  public String getParent() {
    return parent;
  }

  public String getChild() {
    return child;
  }

  public String getLabel() {
    return label;
  }

  public float getImpact() {
    return impact;
  }
}
