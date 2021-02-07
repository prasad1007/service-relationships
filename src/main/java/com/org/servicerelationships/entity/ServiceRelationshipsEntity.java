package com.org.servicerelationships.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SERVICE_RELATIONSHIPS")
public class ServiceRelationshipsEntity implements Serializable {

  @EmbeddedId private ServiceRelationshipsKey serviceRelationshipsKey;

  @Column(name = "LABEL", length = 50)
  private String label;

  @Column(name = "IMPACT")
  private float impact;

  public ServiceRelationshipsEntity(
      final ServiceRelationshipsKey serviceRelationshipsKey,
      final String label,
      final float impact) {
    this.serviceRelationshipsKey = serviceRelationshipsKey;
    this.label = label;
    this.impact = impact;
  }

  public ServiceRelationshipsEntity() {}

  public ServiceRelationshipsKey getServiceRelationshipsKey() {
    return serviceRelationshipsKey;
  }

  public String getLabel() {
    return label;
  }

  public float getImpact() {
    return impact;
  }
}
