package com.org.servicerelationships.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.annotations.VisibleForTesting;
import com.org.servicerelationships.domain.rest.FileLocationRequest;
import com.org.servicerelationships.entity.ServiceRelationshipsEntity;
import com.org.servicerelationships.entity.ServiceRelationshipsKey;
import com.org.servicerelationships.enums.CsvHeaders;
import com.org.servicerelationships.exception.InvalidFileContentTypeException;
import com.org.servicerelationships.exception.ParsingFailedException;
import com.org.servicerelationships.repository.ServiceRelationshipsRepository;

@Service("serviceRelationshipsService")
public class ServiceRelationshipsImpl implements ServiceRelationshipsService {

  private static final String DESIRED_FILE_EXTENSION = "csv";

  private final ServiceRelationshipsRepository serviceRelationshipsRepository;

  public ServiceRelationshipsImpl(
      @Autowired final ServiceRelationshipsRepository serviceRelationshipsRepository) {
    this.serviceRelationshipsRepository = serviceRelationshipsRepository;
  }

  @Override
  public void save(final FileLocationRequest fileLocationRequest)
      throws InvalidFileContentTypeException, FileNotFoundException, ParsingFailedException {

    final File file =
        prepareFileAndCheckIfFileHasDesiredExtension(fileLocationRequest.getFileLocation());

    final List<ServiceRelationshipsEntity> serviceRelationshipsEntityList =
        convertCsvToServiceRelationships(file);
    saveServiceRelationships(serviceRelationshipsEntityList);
  }

  @Override
  public void updateServiceRelationships(final FileLocationRequest fileLocationRequest)
      throws InvalidFileContentTypeException, ParsingFailedException {

    final File file =
        prepareFileAndCheckIfFileHasDesiredExtension(fileLocationRequest.getFileLocation());

    final List<ServiceRelationshipsEntity> serviceRelationshipsEntityList =
        convertCsvToServiceRelationships(file);
    final List<ServiceRelationshipsEntity> existingServiceRelationships =
        (List<ServiceRelationshipsEntity>) serviceRelationshipsRepository.findAll();
    List<ServiceRelationshipsEntity> result = new ArrayList<>();

    final Map<String, ServiceRelationshipsEntity> map = new HashMap<>();
    for (ServiceRelationshipsEntity serviceRelationshipsEntity : existingServiceRelationships) {
      map.put(
          serviceRelationshipsEntity.getServiceRelationshipsKey().getParent()
              + "-"
              + serviceRelationshipsEntity.getServiceRelationshipsKey().getChild(),
          serviceRelationshipsEntity);
    }

    for (ServiceRelationshipsEntity serviceRelationshipsEntity : serviceRelationshipsEntityList) {
      final String key =
          serviceRelationshipsEntity.getServiceRelationshipsKey().getParent()
              + "-"
              + serviceRelationshipsEntity.getServiceRelationshipsKey().getChild();
      if (map.containsKey(key)) {
        map.put(key, serviceRelationshipsEntity);
      }
    }

    for (final Map.Entry<String, ServiceRelationshipsEntity> entry : map.entrySet()) {
      result.add(entry.getValue());
    }

    if (result.isEmpty()) result = serviceRelationshipsEntityList;
    saveServiceRelationships(result);
  }

  private void saveServiceRelationships(final List<ServiceRelationshipsEntity> result) {
    serviceRelationshipsRepository.saveAll(result);
  }

  @VisibleForTesting
  protected File prepareFileAndCheckIfFileHasDesiredExtension(String fileLocation)
      throws InvalidFileContentTypeException {
    final File file = new File(fileLocation);

    if (hasCsvFormat(fileLocation)) {
      throw new InvalidFileContentTypeException("Invalid file content type");
    }
    return file;
  }

  @Override
  public List<ServiceRelationshipsEntity> getServiceRelationships() {
    return (List<ServiceRelationshipsEntity>) serviceRelationshipsRepository.findAll();
  }

  @Override
  public Optional<ServiceRelationshipsEntity> getServiceRelationshipsByParentAndChild(
      final String parent, final String child) {
    return Optional.ofNullable(
        serviceRelationshipsRepository.findByServiceRelationshipsKey(
            new ServiceRelationshipsKey(parent, child)));
  }

  private boolean hasCsvFormat(final String fileLocation) {
    return !DESIRED_FILE_EXTENSION.equals(FilenameUtils.getExtension(fileLocation));
  }

  @VisibleForTesting
  protected List<ServiceRelationshipsEntity> convertCsvToServiceRelationships(final File file)
      throws ParsingFailedException {
    try (BufferedReader csvReader = new BufferedReader(new FileReader(file));
        final CSVParser csvParser =
            new CSVParser(
                csvReader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

      final List<ServiceRelationshipsEntity> serviceRelationshipsEntityList = new ArrayList<>();

      final Iterable<CSVRecord> csvRecords = csvParser.getRecords();

      for (final CSVRecord csvRecord : csvRecords) {
        final ServiceRelationshipsEntity serviceRelationshipsEntity =
            new ServiceRelationshipsEntity(
                new ServiceRelationshipsKey(
                    csvRecord.get(CsvHeaders.PARENT.name()),
                    csvRecord.get(CsvHeaders.CHILD.name())),
                csvRecord.get(CsvHeaders.LABEL.name()),
                Float.parseFloat(csvRecord.get(CsvHeaders.IMPACT.name())));
        serviceRelationshipsEntityList.add(serviceRelationshipsEntity);
      }

      return serviceRelationshipsEntityList;
    } catch (IOException e) {
      throw new ParsingFailedException("fail to parse CSV file: " + e.getMessage());
    }
  }
}
