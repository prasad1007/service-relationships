package com.org.servicerelationships.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.org.servicerelationships.entity.ServiceRelationshipsEntity;
import com.org.servicerelationships.entity.ServiceRelationshipsKey;

@Repository
public interface ServiceRelationshipsRepository
    extends CrudRepository<ServiceRelationshipsEntity, ServiceRelationshipsKey> {

  ServiceRelationshipsEntity findByServiceRelationshipsKey(
      final ServiceRelationshipsKey serviceRelationshipsKey);
}
