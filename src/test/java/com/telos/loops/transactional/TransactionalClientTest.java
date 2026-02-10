package com.telos.loops.transactional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.telos.loops.AsyncTestUtils;
import com.telos.loops.LoopsClient;
import com.telos.loops.TestFixtures;
import com.telos.loops.WireMockSetup;
import com.telos.loops.error.LoopsValidationException;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionalClientTest {

  private WireMockSetup wireMock;
  private LoopsClient client;

  @BeforeEach
  void setUp() {
    wireMock = new WireMockSetup();
    client = wireMock.createClient(TestFixtures.TEST_API_KEY);
  }

  @AfterEach
  void tearDown() {
    wireMock.stop();
  }

  @Test
  void shouldSendTransactionalEmailSuccessfully() {
    // Given
    wireMock.stubPostSuccess("/transactional", """
        {"success": true}
        """);

    // When
    TransactionalResponse response = client.transactional().send(TestFixtures.minimalTransactionalSendRequest());

    // Then
    assertThat(response).isNotNull();
    assertThat(response.success()).isTrue();
    verify(postRequestedFor(urlEqualTo("/transactional"))
        .withHeader("Authorization", equalTo("Bearer " + TestFixtures.TEST_API_KEY)));
  }

  @Test
  void shouldSendTransactionalEmailWithIdempotencyKey() {
    // Given
    wireMock.stubPostSuccess("/transactional", """
        {"success": true}
        """);
    String idempotencyKey = "unique-trans-key";

    // When
    TransactionalResponse response = client.transactional().send(
        TestFixtures.minimalTransactionalSendRequest(), idempotencyKey);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.success()).isTrue();
    verify(postRequestedFor(urlEqualTo("/transactional"))
        .withHeader("Idempotency-Key", equalTo(idempotencyKey)));
  }

  @Test
  void shouldSendTransactionalEmailAsyncSuccessfully() {
    // Given
    wireMock.stubPostSuccess("/transactional", """
        {"success": true}
        """);

    // When
    CompletableFuture<TransactionalResponse> future =
        client.transactional().sendAsync(TestFixtures.minimalTransactionalSendRequest());

    // Then
    TransactionalResponse response = AsyncTestUtils.awaitCompletion(future);
    assertThat(response).isNotNull();
    assertThat(response.success()).isTrue();
  }

  @Test
  void shouldThrowValidationExceptionWhenIdempotencyKeyTooLong() {
    // Given
    String tooLongKey = "a".repeat(101);

    // When/Then
    assertThatThrownBy(() -> client.transactional().send(
        TestFixtures.minimalTransactionalSendRequest(), tooLongKey))
        .isInstanceOf(LoopsValidationException.class)
        .hasMessageContaining("Idempotency key must not exceed 100 characters");
  }

  @Test
  void shouldListTransactionalEmailsSuccessfully() {
    // Given
    wireMock.stubGetSuccess("/transactional", TestFixtures.transactionalListResponse());

    // When
    TransactionalListResponse response = client.transactional().list();

    // Then
    assertThat(response).isNotNull();
    verify(getRequestedFor(urlEqualTo("/transactional")));
  }

  @Test
  void shouldListTransactionalEmailsWithPagination() {
    // Given
    stubFor(get(urlPathEqualTo("/transactional"))
        .withQueryParam("perPage", equalTo("25"))
        .withQueryParam("cursor", equalTo("cursor-123"))
        .willReturn(okJson("""
            {
              "data": [],
              "pagination": {
                "totalPages": 1,
                "totalResults": 0,
                "returnedResults": 0,
                "perPage": 10
              }
            }
            """)));

    // When
    TransactionalListResponse response = client.transactional().list(25, "cursor-123");

    // Then
    assertThat(response).isNotNull();
    verify(getRequestedFor(urlPathEqualTo("/transactional"))
        .withQueryParam("perPage", equalTo("25"))
        .withQueryParam("cursor", equalTo("cursor-123")));
  }

  @Test
  void shouldListTransactionalEmailsAsyncSuccessfully() {
    // Given
    wireMock.stubGetSuccess("/transactional", TestFixtures.transactionalListResponse());

    // When
    CompletableFuture<TransactionalListResponse> future = client.transactional().listAsync();

    // Then
    TransactionalListResponse response = AsyncTestUtils.awaitCompletion(future);
    assertThat(response).isNotNull();
  }

  @Test
  void shouldThrowValidationExceptionWhenPerPageBelowMinimum() {
    // When/Then
    assertThatThrownBy(() -> client.transactional().list(5, null))
        .isInstanceOf(LoopsValidationException.class)
        .hasMessageContaining("perPage must be between 10 and 50");
  }

  @Test
  void shouldThrowValidationExceptionWhenPerPageAboveMaximum() {
    // When/Then
    assertThatThrownBy(() -> client.transactional().list(51, null))
        .isInstanceOf(LoopsValidationException.class)
        .hasMessageContaining("perPage must be between 10 and 50");
  }

  @Test
  void shouldAcceptPerPageAtMinimumBoundary() {
    // Given
    stubFor(get(urlPathEqualTo("/transactional"))
        .withQueryParam("perPage", equalTo("10"))
        .willReturn(okJson("""
            {"data": [], "pagination": {"totalPages": 1, "totalResults": 0, "returnedResults": 0, "perPage": 10}}
            """)));

    // When
    TransactionalListResponse response = client.transactional().list(10, null);

    // Then
    assertThat(response).isNotNull();
  }

  @Test
  void shouldAcceptPerPageAtMaximumBoundary() {
    // Given
    stubFor(get(urlPathEqualTo("/transactional"))
        .withQueryParam("perPage", equalTo("50"))
        .willReturn(okJson("""
            {"data": [], "pagination": {"totalPages": 1, "totalResults": 0, "returnedResults": 0, "perPage": 50}}
            """)));

    // When
    TransactionalListResponse response = client.transactional().list(50, null);

    // Then
    assertThat(response).isNotNull();
  }
}
