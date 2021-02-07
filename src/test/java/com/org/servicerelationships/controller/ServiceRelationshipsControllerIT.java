package com.org.servicerelationships.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.org.servicerelationships.ServiceRelationshipsApplication;
import com.org.servicerelationships.domain.rest.FileLocationRequest;
import com.org.servicerelationships.domain.rest.ServiceRelationshipsRequest;

@SpringBootTest(classes = ServiceRelationshipsApplication.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class ServiceRelationshipsControllerIT {

  public static final String INPUT_CSV_FILE_PATH =
      "src/test/resources/files/servicerelationships.csv";
  public static final String INPUT_CSV_FILE_PATH_FOR_UPDATE =
      "src/test/resources/files/servicerelationships_tobeupdated.csv";
  public static final String INPUT_FILE_PATH_NOT_FOUND =
      "src/test/resources/files/servicerelationships";

  @LocalServerPort private int port;

  final TestRestTemplate restTemplate = new TestRestTemplate();

  @Test
  void testIfCsvSavedOK() {
    final FileLocationRequest fileLocationRequest = new FileLocationRequest(INPUT_CSV_FILE_PATH);

    final ResponseEntity<Void> responseEntity =
        restTemplate.postForEntity(
            createURLWithPort("/servicerelationships/uploadCsv"), fileLocationRequest, Void.class);

    assertThat(responseEntity.getStatusCode(), is(CREATED));
  }

  @Test
  void testIfCsvWithInalidExtensionUploaded() {
    final FileLocationRequest fileLocationRequest =
        new FileLocationRequest(INPUT_FILE_PATH_NOT_FOUND);

    final ResponseEntity<Void> responseEntity =
        restTemplate.postForEntity(
            createURLWithPort("/servicerelationships/uploadCsv"), fileLocationRequest, Void.class);

    assertThat(responseEntity.getStatusCode(), is(BAD_REQUEST));
  }

  @Test
  void testIfAllServiceRelationshipsFetched() {
    final ServiceRelationshipsRequest serviceRelationshipsRequest =
        new ServiceRelationshipsRequest("global", "asia", "global-asia", 1.0F);

    final ResponseEntity<Void> responseEntity =
        restTemplate.postForEntity(
            createURLWithPort("/servicerelationships/uploadCsv"),
            serviceRelationshipsRequest,
            Void.class);
    final ResponseEntity<Void> responseEntity1 =
        restTemplate.getForEntity(
            createURLWithPort("/servicerelationships/getAllServiceRelationships"), Void.class);

    assertThat(responseEntity1.getStatusCode(), is(OK));
  }

  @Test
  void testIfServiceRelationshipsFetchedByParentAndChild() {
    final FileLocationRequest fileLocationRequest = new FileLocationRequest(INPUT_CSV_FILE_PATH);

    final ResponseEntity<Void> responseEntity =
        restTemplate.postForEntity(
            createURLWithPort("/servicerelationships/uploadCsv"), fileLocationRequest, Void.class);

    assertThat(responseEntity.getStatusCode(), is(CREATED));

    final ResponseEntity<Void> responseEntity1 =
        restTemplate.getForEntity(
            createURLWithPort(
                "/servicerelationships/getServiceRelationshipsByParentAndChild/global/asia"),
            Void.class);

    assertThat(responseEntity1.getStatusCode(), is(OK));
  }

  @Test
  void testIfServiceRelationshipsFetchedByParentAndChildIsNull() {
    final FileLocationRequest fileLocationRequest = new FileLocationRequest(INPUT_CSV_FILE_PATH);

    final ResponseEntity<Void> responseEntity =
        restTemplate.postForEntity(
            createURLWithPort("/servicerelationships/uploadCsv"), fileLocationRequest, Void.class);
    final ResponseEntity<Void> responseEntity1 =
        restTemplate.getForEntity(
            createURLWithPort(
                "/servicerelationships/getServiceRelationshipsByParentAndChild/global1/asia"),
            Void.class);

    assertThat(responseEntity1.getStatusCode(), is(NOT_FOUND));
  }

  @Test
  void testIfServiceRelationshipsUpdatesOk() {
    final FileLocationRequest fileLocationRequest = new FileLocationRequest(INPUT_CSV_FILE_PATH);
    final FileLocationRequest fileLocationRequest1 = new FileLocationRequest(INPUT_CSV_FILE_PATH);

    final ResponseEntity<Void> responseEntity =
        restTemplate.postForEntity(
            createURLWithPort("/servicerelationships/uploadCsv"), fileLocationRequest, Void.class);
    final ResponseEntity<Void> responseEntity1 =
        restTemplate.exchange(
            createURLWithPort("/servicerelationships/compareAndUpdate"),
            HttpMethod.PUT,
            new HttpEntity<>(fileLocationRequest1),
            Void.class);

    assertThat(responseEntity1.getStatusCode(), is(OK));
  }

  private String createURLWithPort(final String uri) {
    return "http://localhost:" + port + uri;
  }
}
