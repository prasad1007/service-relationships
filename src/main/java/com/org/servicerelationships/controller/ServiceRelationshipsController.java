package com.org.servicerelationships.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.org.servicerelationships.constant.UriConstant;
import com.org.servicerelationships.domain.rest.FileLocationRequest;
import com.org.servicerelationships.domain.rest.ServiceRelationshipsResponse;
import com.org.servicerelationships.exception.DataNotFoundException;
import com.org.servicerelationships.exception.InvalidFileContentTypeException;
import com.org.servicerelationships.exception.ParsingFailedException;
import com.org.servicerelationships.mappers.CoreToRestMapper;
import com.org.servicerelationships.service.ServiceRelationshipsService;

@RestController
@RequestMapping(value = UriConstant.API)
public class ServiceRelationshipsController {

  private final ServiceRelationshipsService serviceRelationshipsService;

  private final CoreToRestMapper coreToRestMapper;

  @Autowired
  public ServiceRelationshipsController(
      final ServiceRelationshipsService serviceRelationshipsService,
      final CoreToRestMapper coreToRestMapper) {
    this.serviceRelationshipsService = serviceRelationshipsService;
    this.coreToRestMapper = coreToRestMapper;
  }

  @PostMapping(value = UriConstant.UPLOAD_CSV)
  @ResponseStatus(HttpStatus.CREATED)
  public void uploadCsv(final @RequestBody FileLocationRequest fileLocationRequest)
      throws InvalidFileContentTypeException, IOException, ParsingFailedException {
    serviceRelationshipsService.save(fileLocationRequest);
  }

  @GetMapping(UriConstant.GET_ALL_SERVICE_RELATIONSHIPS)
  @ResponseStatus(HttpStatus.OK)
  public List<ServiceRelationshipsResponse> getServiceRelationships() throws DataNotFoundException {
    return coreToRestMapper.mapCoreToRestServiceRelationships(
        serviceRelationshipsService.getServiceRelationships());
  }

  @GetMapping(UriConstant.GET_SERVICE_RELATIONSHIPS_BY_PARENT_AND_CHILD)
  @ResponseStatus(HttpStatus.OK)
  public ServiceRelationshipsResponse getServiceRelationshipsByParentAndChild(
      @PathVariable final String parent, @PathVariable final String child)
      throws DataNotFoundException {
    return coreToRestMapper.mapServiceRelationship(
        serviceRelationshipsService.getServiceRelationshipsByParentAndChild(parent, child));
  }

  @PutMapping(UriConstant.COMPARE_AND_UPDATE)
  @ResponseStatus(HttpStatus.OK)
  public void updateServiceRelationships(final @RequestBody FileLocationRequest fileLocationRequest)
      throws InvalidFileContentTypeException, FileNotFoundException, ParsingFailedException {
    serviceRelationshipsService.updateServiceRelationships(fileLocationRequest);
  }
}
