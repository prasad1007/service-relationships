package com.org.servicerelationships.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.org.servicerelationships.domain.rest.ServiceRelationshipsResponse;
import com.org.servicerelationships.entity.ServiceRelationshipsEntity;
import com.org.servicerelationships.exception.DataNotFoundException;

@Component
public class CoreToRestMapper {

  public List<ServiceRelationshipsResponse> mapCoreToRestServiceRelationships(
      final List<ServiceRelationshipsEntity> serviceRelationshipsEntities)
      throws DataNotFoundException {
    List<ServiceRelationshipsResponse> serviceRelationshipsResponses = new ArrayList<>();
    for (ServiceRelationshipsEntity serviceRelationshipsEntity : serviceRelationshipsEntities) {
      ServiceRelationshipsResponse serviceRelationshipsResponse =
          mapServiceRelationship(Optional.of(serviceRelationshipsEntity));
      serviceRelationshipsResponses.add(serviceRelationshipsResponse);
    }

    if (serviceRelationshipsResponses.isEmpty()) {
      throw new DataNotFoundException("Data not found");
    }

    return serviceRelationshipsResponses;
  }

  public ServiceRelationshipsResponse mapServiceRelationship(
      final Optional<ServiceRelationshipsEntity> serviceRelationshipsEntity)
      throws DataNotFoundException {

    return serviceRelationshipsEntity
        .map(
            sre ->
                new ServiceRelationshipsResponse(
                    sre.getServiceRelationshipsKey().getParent(),
                    sre.getServiceRelationshipsKey().getChild(),
                    sre.getLabel(),
                    sre.getImpact()))
        .orElseThrow(() -> new DataNotFoundException("Data not found"));
  }
}
