package com.telos.loops.events;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.telos.loops.AsyncTestUtils;
import com.telos.loops.LoopsClient;
import com.telos.loops.TestFixtures;
import com.telos.loops.WireMockSetup;
import com.telos.loops.error.LoopsApiException;
import com.telos.loops.error.LoopsValidationException;
import com.telos.loops.error.RateLimitExceededException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventsClientTest {

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

  // ============================================================
  // Send Operation Tests
  // ============================================================

  @Test
  void shouldSendEventSuccessfully() {
    // Given
    wireMock.stubPostSuccess("/events/send", TestFixtures.eventSendSuccessResponse());

    // When
    EventResponse response = client.events().send(TestFixtures.minimalEventSendRequest());

    // Then
    assertThat(response).isNotNull();
    assertThat(response.success()).isTrue();
    verify(
        postRequestedFor(urlEqualTo("/events/send"))
            .withHeader("Authorization", equalTo("Bearer " + TestFixtures.TEST_API_KEY))
            .withHeader("Content-Type", equalTo("application/json")));
  }

  @Test
  void shouldSendEventWithProperties() {
    // Given
    wireMock.stubPostSuccess("/events/send", TestFixtures.eventSendSuccessResponse());

    // When
    EventResponse response = client.events().send(TestFixtures.eventSendRequestWithProperties());

    // Then
    assertThat(response).isNotNull();
    assertThat(response.success()).isTrue();
    verify(
        postRequestedFor(urlEqualTo("/events/send"))
            .withRequestBody(matchingJsonPath("$.eventName", equalTo("PurchaseCompleted")))
            .withRequestBody(matchingJsonPath("$.eventProperties.plan", equalTo("pro")))
            .withRequestBody(matchingJsonPath("$.eventProperties.amount", equalTo("99.99"))));
  }

  @Test
  void shouldSendEventWithIdempotencyKey() {
    // Given
    wireMock.stubPostSuccess("/events/send", TestFixtures.eventSendSuccessResponse());
    String idempotencyKey = "unique-key-123";

    // When
    EventResponse response =
        client.events().send(TestFixtures.minimalEventSendRequest(), idempotencyKey);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.success()).isTrue();
    verify(
        postRequestedFor(urlEqualTo("/events/send"))
            .withHeader("Idempotency-Key", equalTo(idempotencyKey)));
  }

  @Test
  void shouldSendEventAsyncSuccessfully() {
    // Given
    wireMock.stubPostSuccess("/events/send", TestFixtures.eventSendSuccessResponse());

    // When
    CompletableFuture<EventResponse> future =
        client.events().sendAsync(TestFixtures.minimalEventSendRequest());

    // Then
    EventResponse response = AsyncTestUtils.awaitCompletion(future);
    assertThat(response).isNotNull();
    assertThat(response.success()).isTrue();
  }

  @Test
  void shouldSendEventAsyncWithIdempotencyKey() {
    // Given
    wireMock.stubPostSuccess("/events/send", TestFixtures.eventSendSuccessResponse());
    String idempotencyKey = "async-key-456";

    // When
    CompletableFuture<EventResponse> future =
        client.events().sendAsync(TestFixtures.minimalEventSendRequest(), idempotencyKey);

    // Then
    EventResponse response = AsyncTestUtils.awaitCompletion(future);
    assertThat(response).isNotNull();
    assertThat(response.success()).isTrue();
    verify(
        postRequestedFor(urlEqualTo("/events/send"))
            .withHeader("Idempotency-Key", equalTo(idempotencyKey)));
  }

  // ============================================================
  // Idempotency Key Validation Tests
  // ============================================================

  @Test
  void shouldThrowValidationExceptionWhenIdempotencyKeyTooLong() {
    // Given
    String tooLongKey = "a".repeat(101); // 101 characters, exceeds 100 limit

    // When/Then
    LoopsValidationException exception =
        catchThrowableOfType(
            () -> client.events().send(TestFixtures.minimalEventSendRequest(), tooLongKey),
            LoopsValidationException.class);

    assertThat(exception.getMessage())
        .contains("Idempotency key must not exceed 100 characters")
        .contains("got: 101");
  }

  @Test
  void shouldAcceptIdempotencyKeyAtMaxLength() {
    // Given
    wireMock.stubPostSuccess("/events/send", TestFixtures.eventSendSuccessResponse());
    String maxLengthKey = "a".repeat(100); // Exactly 100 characters

    // When
    EventResponse response =
        client.events().send(TestFixtures.minimalEventSendRequest(), maxLengthKey);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.success()).isTrue();
    verify(
        postRequestedFor(urlEqualTo("/events/send"))
            .withHeader("Idempotency-Key", equalTo(maxLengthKey)));
  }

  @Test
  void shouldThrowValidationExceptionInAsyncWhenIdempotencyKeyTooLong() {
    // Given
    String tooLongKey = "b".repeat(150);

    // When/Then
    assertThatThrownBy(
            () -> client.events().sendAsync(TestFixtures.minimalEventSendRequest(), tooLongKey))
        .isInstanceOf(LoopsValidationException.class)
        .hasMessageContaining("Idempotency key must not exceed 100 characters")
        .hasMessageContaining("got: 150");
  }

  @Test
  void shouldAllowNullIdempotencyKey() {
    // Given
    wireMock.stubPostSuccess("/events/send", TestFixtures.eventSendSuccessResponse());

    // When
    EventResponse response = client.events().send(TestFixtures.minimalEventSendRequest(), null);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.success()).isTrue();
    verify(postRequestedFor(urlEqualTo("/events/send")).withoutHeader("Idempotency-Key"));
  }

  // ============================================================
  // Error Handling Tests
  // ============================================================

  @Test
  void shouldThrowLoopsApiExceptionOn400ValidationError() {
    // Given
    wireMock.stubValidationError("/events/send");

    // When/Then
    LoopsApiException exception =
        catchThrowableOfType(
            () -> client.events().send(TestFixtures.minimalEventSendRequest()),
            LoopsApiException.class);

    assertThat(exception.statusCode()).isEqualTo(400);
    assertThat(exception.rawBody()).contains("Invalid email format");
  }

  @Test
  void shouldThrowLoopsApiExceptionOn401Unauthorized() {
    // Given
    wireMock.stubUnauthorized("/events/send");

    // When/Then
    LoopsApiException exception =
        catchThrowableOfType(
            () -> client.events().send(TestFixtures.minimalEventSendRequest()),
            LoopsApiException.class);

    assertThat(exception.statusCode()).isEqualTo(401);
    assertThat(exception.rawBody()).contains("Invalid API key");
  }

  @Test
  void shouldThrowRateLimitExceptionOn429WithRetryAfter() {
    // Given
    wireMock.stubRateLimit("/events/send", 90);

    // When/Then
    RateLimitExceededException exception =
        catchThrowableOfType(
            () -> client.events().send(TestFixtures.minimalEventSendRequest()),
            RateLimitExceededException.class);

    assertThat(exception.statusCode()).isEqualTo(429);
    assertThat(exception.retryAfterSeconds()).isEqualTo(90);
  }

  @Test
  void shouldThrowLoopsApiExceptionOn500ServerError() {
    // Given
    wireMock.stubServerError("/events/send");

    // When/Then
    LoopsApiException exception =
        catchThrowableOfType(
            () -> client.events().send(TestFixtures.minimalEventSendRequest()),
            LoopsApiException.class);

    assertThat(exception.statusCode()).isEqualTo(500);
    assertThat(exception.rawBody()).contains("Internal server error");
  }

  @Test
  void shouldPropagateErrorInAsyncOperation() {
    // Given
    wireMock.stubValidationError("/events/send");

    // When
    CompletableFuture<EventResponse> future =
        client.events().sendAsync(TestFixtures.minimalEventSendRequest());

    // Then
    LoopsApiException exception = AsyncTestUtils.awaitException(future, LoopsApiException.class);
    assertThat(exception.statusCode()).isEqualTo(400);
  }

  @Test
  void shouldPropagateRateLimitErrorInAsyncOperation() {
    // Given
    wireMock.stubRateLimit("/events/send", 45);

    // When
    CompletableFuture<EventResponse> future =
        client.events().sendAsync(TestFixtures.minimalEventSendRequest());

    // Then
    RateLimitExceededException exception =
        AsyncTestUtils.awaitException(future, RateLimitExceededException.class);
    assertThat(exception.statusCode()).isEqualTo(429);
    assertThat(exception.retryAfterSeconds()).isEqualTo(45);
  }

  // ============================================================
  // Event Name Validation Tests
  // ============================================================

  @Test
  void shouldThrowValidationExceptionWhenEventNameIsNull() {
    // When/Then
    assertThatThrownBy(() -> new EventSendRequest(TestFixtures.TEST_EMAIL, null, null, null, null, null))
        .isInstanceOf(LoopsValidationException.class)
        .hasMessageContaining("eventName is required");
  }

  @Test
  void shouldThrowValidationExceptionWhenEventNameIsBlank() {
    // When/Then
    assertThatThrownBy(() -> new EventSendRequest(TestFixtures.TEST_EMAIL, null, "  ", null, null, null))
        .isInstanceOf(LoopsValidationException.class)
        .hasMessageContaining("eventName is required");
  }
}
