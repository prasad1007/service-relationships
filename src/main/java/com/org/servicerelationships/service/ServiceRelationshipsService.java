package com.org.servicerelationships.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.org.servicerelationships.domain.rest.FileLocationRequest;
import com.org.servicerelationships.entity.ServiceRelationshipsEntity;
import com.org.servicerelationships.exception.InvalidFileContentTypeException;
import com.org.servicerelationships.exception.ParsingFailedException;

@Service
public interface ServiceRelationshipsService {

  void save(final FileLocationRequest fileLocationRequest)
      throws InvalidFileContentTypeException, IOException, ParsingFailedException;

  void updateServiceRelationships(final FileLocationRequest fileLocationRequest)
      throws InvalidFileContentTypeException, FileNotFoundException, ParsingFailedException;

  List<ServiceRelationshipsEntity> getServiceRelationships();

  Optional<ServiceRelationshipsEntity> getServiceRelationshipsByParentAndChild(
      final String parent, final String child);
}
