package com.org.servicerelationships.mappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.org.servicerelationships.domain.rest.ServiceRelationshipsResponse;
import com.org.servicerelationships.entity.ServiceRelationshipsEntity;
import com.org.servicerelationships.entity.ServiceRelationshipsKey;
import com.org.servicerelationships.exception.DataNotFoundException;

class CoreToRestMapperTest {

  final CoreToRestMapper coreToRestMapper = new CoreToRestMapper();

  @Test
  void testIfMapCoreToRestServiceRelationshipsMapsOk() throws DataNotFoundException {
    final List<ServiceRelationshipsEntity> serviceRelationshipsEntities = new ArrayList<>();
    final ServiceRelationshipsEntity serviceRelationshipsEntity =
        new ServiceRelationshipsEntity(
            new ServiceRelationshipsKey("global", "asia"), "global-asia", 1.0F);
    final ServiceRelationshipsEntity serviceRelationshipsEntity1 =
        new ServiceRelationshipsEntity(
            new ServiceRelationshipsKey("global", "asia"), "global-asia", 1.0F);
    serviceRelationshipsEntities.add(serviceRelationshipsEntity);
    serviceRelationshipsEntities.add(serviceRelationshipsEntity1);

    final List<ServiceRelationshipsResponse> serviceRelationshipsResponses =
        coreToRestMapper.mapCoreToRestServiceRelationships(serviceRelationshipsEntities);

    assertThat(serviceRelationshipsResponses.size(), is(2));
  }

  @Test
  void testIfMapServiceRelationshipHandlesEmptyResponse() {
    assertThrows(
        DataNotFoundException.class,
        () -> coreToRestMapper.mapServiceRelationship(Optional.empty()));
  }
}
