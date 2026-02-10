package com.telos.loops.error;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ErrorHandlingTest {

  @Test
  void shouldCreateLoopsApiExceptionWithMessage() {
    // When
    LoopsApiException exception = new LoopsApiException("Test error message");

    // Then
    assertThat(exception.getMessage()).isEqualTo("Test error message");
    assertThat(exception.statusCode()).isZero();
    assertThat(exception.rawBody()).isNull();
    assertThat(exception.error()).isNull();
  }

  @Test
  void shouldCreateLoopsApiExceptionWithStatusCodeAndBody() {
    // Given
    String rawBody = """
        {"error": "Invalid request"}
        """;

    // When
    LoopsApiException exception = new LoopsApiException(400, rawBody);

    // Then
    assertThat(exception.statusCode()).isEqualTo(400);
    assertThat(exception.rawBody()).isEqualTo(rawBody);
    assertThat(exception.error()).isEqualTo("Invalid request");
    assertThat(exception.getMessage()).contains("HTTP 400");
    assertThat(exception.getMessage()).contains("Invalid request");
  }

  @Test
  void shouldExtractErrorMessageFromJsonResponse() {
    // Given
    String rawBody = """
        {"message": "Email is required"}
        """;

    // When
    LoopsApiException exception = new LoopsApiException(400, rawBody);

    // Then
    assertThat(exception.getMessage()).contains("Email is required");
  }

  @Test
  void shouldHandleMalformedJsonGracefully() {
    // Given
    String rawBody = "Not valid JSON";

    // When
    LoopsApiException exception = new LoopsApiException(500, rawBody);

    // Then
    assertThat(exception.statusCode()).isEqualTo(500);
    assertThat(exception.rawBody()).isEqualTo(rawBody);
    assertThat(exception.error()).isNull();
  }

  @Test
  void shouldCreateRateLimitExceptionWithRetryAfter() {
    // Given
    String rawBody = """
        {"error": "Rate limit exceeded"}
        """;

    // When
    RateLimitExceededException exception = new RateLimitExceededException(429, rawBody, 60);

    // Then
    assertThat(exception.statusCode()).isEqualTo(429);
    assertThat(exception.rawBody()).isEqualTo(rawBody);
    assertThat(exception.retryAfterSeconds()).isEqualTo(60);
    assertThat(exception).isInstanceOf(LoopsApiException.class);
  }

  @Test
  void shouldCreateRateLimitExceptionWithZeroRetryAfter() {
    // When
    RateLimitExceededException exception = new RateLimitExceededException(429, "{}", 0);

    // Then
    assertThat(exception.retryAfterSeconds()).isZero();
  }

  @Test
  void shouldCreateLoopsValidationException() {
    // When
    LoopsValidationException exception = new LoopsValidationException("Validation failed");

    // Then
    assertThat(exception.getMessage()).isEqualTo("Validation failed");
    assertThat(exception).isInstanceOf(RuntimeException.class);
  }

  @Test
  void shouldBeRuntimeExceptionsForUncheckedBehavior() {
    // Then
    assertThat(new LoopsApiException("test")).isInstanceOf(RuntimeException.class);
    assertThat(new RateLimitExceededException(429, "{}", 0)).isInstanceOf(RuntimeException.class);
    assertThat(new LoopsValidationException("test")).isInstanceOf(RuntimeException.class);
  }
}
