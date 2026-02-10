package com.telos.loops.apikey;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.telos.loops.AsyncTestUtils;
import com.telos.loops.LoopsClient;
import com.telos.loops.TestFixtures;
import com.telos.loops.WireMockSetup;
import com.telos.loops.error.LoopsApiException;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApiKeyClientTest {

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
  void shouldTestApiKeySuccessfully() {
    // Given
    wireMock.stubGetSuccess("/api-key", TestFixtures.apiKeyTestSuccessResponse());

    // When
    ApiKeyTestResponse response = client.apiKey().test();

    // Then
    assertThat(response).isNotNull();
    assertThat(response.success()).isTrue();
    assertThat(response.teamName()).isEqualTo("Test Team");
    verify(getRequestedFor(urlEqualTo("/api-key"))
        .withHeader("Authorization", equalTo("Bearer " + TestFixtures.TEST_API_KEY)));
  }

  @Test
  void shouldTestApiKeyAsyncSuccessfully() {
    // Given
    wireMock.stubGetSuccess("/api-key", TestFixtures.apiKeyTestSuccessResponse());

    // When
    CompletableFuture<ApiKeyTestResponse> future = client.apiKey().testAsync();

    // Then
    ApiKeyTestResponse response = AsyncTestUtils.awaitCompletion(future);
    assertThat(response).isNotNull();
    assertThat(response.success()).isTrue();
    assertThat(response.teamName()).isEqualTo("Test Team");
  }

  @Test
  void shouldThrowLoopsApiExceptionOnInvalidApiKey() {
    // Given
    stubFor(get(urlEqualTo("/api-key"))
        .willReturn(aResponse()
            .withStatus(401)
            .withBody("""
                {"error": "Invalid API key"}
                """)));

    // When/Then
    LoopsApiException exception = catchThrowableOfType(
        () -> client.apiKey().test(),
        LoopsApiException.class);

    assertThat(exception.statusCode()).isEqualTo(401);
    assertThat(exception.rawBody()).contains("Invalid API key");
  }

  @Test
  void shouldPropagateErrorInAsyncOperation() {
    // Given
    stubFor(get(urlEqualTo("/api-key"))
        .willReturn(aResponse()
            .withStatus(401)
            .withBody("""
                {"error": "Invalid API key"}
                """)));

    // When
    CompletableFuture<ApiKeyTestResponse> future = client.apiKey().testAsync();

    // Then
    LoopsApiException exception = AsyncTestUtils.awaitException(future, LoopsApiException.class);
    assertThat(exception.statusCode()).isEqualTo(401);
  }
}
