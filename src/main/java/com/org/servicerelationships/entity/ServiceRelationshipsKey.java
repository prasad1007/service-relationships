package com.org.servicerelationships.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ServiceRelationshipsKey implements Serializable {

  @Column(name = "PARENT", length = 50)
  private String parent;

  @Column(name = "CHILD", length = 50)
  private String child;

  public ServiceRelationshipsKey(final String parent, final String child) {
    this.parent = parent;
    this.child = child;
  }

  public ServiceRelationshipsKey() {}

  public String getParent() {
    return parent;
  }

  public String getChild() {
    return child;
  }
}
