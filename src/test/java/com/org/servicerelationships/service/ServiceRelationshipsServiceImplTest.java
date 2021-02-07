package com.org.servicerelationships.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.org.servicerelationships.domain.rest.FileLocationRequest;
import com.org.servicerelationships.entity.ServiceRelationshipsEntity;
import com.org.servicerelationships.entity.ServiceRelationshipsKey;
import com.org.servicerelationships.exception.InvalidFileContentTypeException;
import com.org.servicerelationships.exception.ParsingFailedException;
import com.org.servicerelationships.repository.ServiceRelationshipsRepository;

class ServiceRelationshipsServiceImplTest {

  public static final String INPUT_CSV_FILE_PATH =
      "src/test/resources/files/servicerelationships.csv";
  public static final String INPUT_CSV_FILE_PATH_FOR_UPDATE =
      "src/test/resources/files/servicerelationships_tobeupdated.csv";
  public static final String INPUT_FILE_PATH_NOT_FOUND =
      "src/test/resources/files/servicerelationships";

  private final ServiceRelationshipsRepository serviceRelationshipsRepository =
      mock(ServiceRelationshipsRepository.class);
  private final ServiceRelationshipsImpl serviceRelationshipsService =
      new ServiceRelationshipsImpl(serviceRelationshipsRepository);

  @Test
  void testIfCsvIsParsedCorrectly() throws FileNotFoundException, ParsingFailedException {
    final List<ServiceRelationshipsEntity> serviceRelationshipsEntities =
        serviceRelationshipsService.convertCsvToServiceRelationships(new File(INPUT_CSV_FILE_PATH));

    assertThat(serviceRelationshipsEntities.size(), is(1));
    assertThat(
        serviceRelationshipsEntities.get(0).getServiceRelationshipsKey().getParent(), is("global"));
    assertThat(
        serviceRelationshipsEntities.get(0).getServiceRelationshipsKey().getChild(), is("asia"));
    assertThat(serviceRelationshipsEntities.get(0).getLabel(), is("global-asia"));
    assertThat(serviceRelationshipsEntities.get(0).getImpact(), is(123456.0F));
  }

  @Test
  void testIfParsingFailsIfFileNotFound() {
    assertThrows(
        ParsingFailedException.class,
        () ->
            serviceRelationshipsService.convertCsvToServiceRelationships(
                new File(INPUT_FILE_PATH_NOT_FOUND)));
  }

  @Test
  void testIfFileIsPreparedCorrectlyIfFileExtensionIsCsv() throws InvalidFileContentTypeException {
    assertThat(
        serviceRelationshipsService.prepareFileAndCheckIfFileHasDesiredExtension(
            INPUT_CSV_FILE_PATH),
        is(new File(INPUT_CSV_FILE_PATH)));
  }

  @Test
  void testIfFilePreparationFailsIfFileExtensionIsInvalid() {
    assertThrows(
        InvalidFileContentTypeException.class,
        () ->
            serviceRelationshipsService.prepareFileAndCheckIfFileHasDesiredExtension(
                INPUT_FILE_PATH_NOT_FOUND));
  }

  @Test
  void testIfFileGetsSavedOk() {
    final FileLocationRequest fileLocationRequest = new FileLocationRequest(INPUT_CSV_FILE_PATH);
    final List<ServiceRelationshipsEntity> serviceRelationshipsEntities = new ArrayList<>();
    serviceRelationshipsEntities.add(
        new ServiceRelationshipsEntity(
            new ServiceRelationshipsKey("global", "asia"), "global-asia", 123456F));

    when(serviceRelationshipsRepository.saveAll(any())).thenReturn(serviceRelationshipsEntities);

    assertDoesNotThrow(() -> serviceRelationshipsService.save(fileLocationRequest));

    verify(serviceRelationshipsRepository, times(1)).saveAll(any());
  }

  @Test
  void testIfFileGetsUpdatedOk() {
    final FileLocationRequest fileLocationRequest =
        new FileLocationRequest(INPUT_CSV_FILE_PATH_FOR_UPDATE);
    final List<ServiceRelationshipsEntity> serviceRelationshipsEntities = new ArrayList<>();
    serviceRelationshipsEntities.add(
        new ServiceRelationshipsEntity(
            new ServiceRelationshipsKey("global", "asia"), "global-asia", 123456F));

    when(serviceRelationshipsRepository.findAll()).thenReturn(serviceRelationshipsEntities);

    final List<ServiceRelationshipsEntity> serviceRelationshipsEntities1 = new ArrayList<>();
    serviceRelationshipsEntities1.add(
        new ServiceRelationshipsEntity(
            new ServiceRelationshipsKey("global", "asia"), "global-asia12345", 123456F));

    when(serviceRelationshipsRepository.saveAll(serviceRelationshipsEntities1))
        .thenReturn(serviceRelationshipsEntities1);

    assertDoesNotThrow(
        () -> serviceRelationshipsService.updateServiceRelationships(fileLocationRequest));

    verify(serviceRelationshipsRepository, times(1)).findAll();
    verify(serviceRelationshipsRepository, times(1)).saveAll(any());
  }

  @Test
  void testIfGettingAllServiceRelationShips() {
    final List<ServiceRelationshipsEntity> serviceRelationshipsEntities = new ArrayList<>();
    serviceRelationshipsEntities.add(
        new ServiceRelationshipsEntity(
            new ServiceRelationshipsKey("global", "asia"), "global-asia", 123456F));
    serviceRelationshipsEntities.add(
        new ServiceRelationshipsEntity(
            new ServiceRelationshipsKey("global", "europe"), "global-europe", 123456F));

    serviceRelationshipsService.getServiceRelationships();

    when(serviceRelationshipsRepository.findAll()).thenReturn(serviceRelationshipsEntities);

    verify(serviceRelationshipsRepository, times(1)).findAll();
  }

  @Test
  void testIfGettingServiceRelationShipsByParentAndChild() {
    final ServiceRelationshipsEntity serviceRelationshipsEntity =
        new ServiceRelationshipsEntity(
            new ServiceRelationshipsKey("global", "asia"), "global-asia", 123456F);

    serviceRelationshipsService.getServiceRelationshipsByParentAndChild("global", "asia");

    when(serviceRelationshipsRepository.findByServiceRelationshipsKey(any()))
        .thenReturn(serviceRelationshipsEntity);

    verify(serviceRelationshipsRepository, times(1)).findByServiceRelationshipsKey(any());
  }

  @Test
  void testIfServiceRelationShipsByParentAndChildNotFound() {
    final ServiceRelationshipsEntity serviceRelationshipsEntity =
        new ServiceRelationshipsEntity(
            new ServiceRelationshipsKey("global1", "asia1"), "global-asia", 123456F);

    serviceRelationshipsService.getServiceRelationshipsByParentAndChild("global", "asia");

    when(serviceRelationshipsRepository.findByServiceRelationshipsKey(any()))
        .thenReturn(serviceRelationshipsEntity);

    verify(serviceRelationshipsRepository, times(1)).findByServiceRelationshipsKey(any());
  }
}
