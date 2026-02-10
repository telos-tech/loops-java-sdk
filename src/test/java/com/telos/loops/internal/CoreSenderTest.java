package com.telos.loops.internal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.telos.loops.TestFixtures;
import com.telos.loops.contacts.ContactResponse;
import com.telos.loops.error.LoopsApiException;
import com.telos.loops.error.RateLimitExceededException;
import com.telos.loops.model.RequestOptions;
import com.telos.loops.transport.OkHttpTransport;
import com.telos.loops.transport.Transport;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreSenderTest {

  private WireMockServer wireMockServer;
  private CoreSender coreSender;
  private String baseUrl;

  @BeforeEach
  void setUp() {
    wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
    wireMockServer.start();
    baseUrl = "http://localhost:" + wireMockServer.port();
    configureFor("localhost", wireMockServer.port());

    Transport transport = new OkHttpTransport();
    coreSender = new CoreSender(transport, baseUrl, TestFixtures.TEST_API_KEY);
  }

  @AfterEach
  void tearDown() {
    wireMockServer.stop();
  }

  @Test
  void shouldSendPostRequestWithJsonBodyAndHeaders() {
    // Given
    stubFor(
        post(urlEqualTo("/contacts/create"))
            .willReturn(okJson("""
                {"id": "contact-123", "message": "Success"}
                """)));

    // When
    ContactResponse response =
        coreSender.postJson(
            "/contacts/create",
            TestFixtures.minimalContactCreateRequest(),
            ContactResponse.class,
            RequestOptions.none());

    // Then
    assertThat(response.id()).isNotNull();
    verify(
        postRequestedFor(urlEqualTo("/contacts/create"))
            .withHeader("Authorization", equalTo("Bearer " + TestFixtures.TEST_API_KEY))
            .withHeader("Content-Type", equalTo("application/json")));
  }

  @Test
  void shouldSendGetRequestWithQueryParameters() {
    // Given
    stubFor(
        get(urlPathEqualTo("/contacts/find"))
            .withQueryParam("email", equalTo("user@example.com"))
            .willReturn(okJson("""
                {"id": "contact-123", "message": "Success"}
                """)));

    // When
    ContactResponse response =
        coreSender.get(
            "/contacts/find",
            Map.of("email", "user@example.com"),
            ContactResponse.class,
            RequestOptions.none());

    // Then
    assertThat(response.id()).isNotNull();
    verify(
        getRequestedFor(urlPathEqualTo("/contacts/find"))
            .withQueryParam("email", equalTo("user@example.com"))
            .withHeader("Authorization", equalTo("Bearer " + TestFixtures.TEST_API_KEY)));
  }

  @Test
  void shouldIncludeCustomHeadersFromRequestOptions() {
    // Given
    stubFor(post(urlEqualTo("/test")).willReturn(okJson("""
        {"id": "contact-123", "message": "Success"}
        """)));

    RequestOptions options =
        new RequestOptions(
            Map.of("X-Custom-Header", "custom-value", "X-Request-Id", "req-123"));

    // When
    coreSender.postJson(
        "/test", TestFixtures.minimalEventSendRequest(), ContactResponse.class, options);

    // Then
    verify(
        postRequestedFor(urlEqualTo("/test"))
            .withHeader("X-Custom-Header", equalTo("custom-value"))
            .withHeader("X-Request-Id", equalTo("req-123")));
  }

  @Test
  void shouldThrowRateLimitExceptionOn429WithRetryAfterHeader() {
    // Given
    stubFor(
        post(urlEqualTo("/events/send"))
            .willReturn(
                aResponse()
                    .withStatus(429)
                    .withHeader("Retry-After", "60")
                    .withBody(TestFixtures.rateLimitErrorResponse())));

    // When/Then
    RateLimitExceededException exception =
        catchThrowableOfType(
            () ->
                coreSender.postJson(
                    "/events/send",
                    TestFixtures.minimalEventSendRequest(),
                    ContactResponse.class,
                    RequestOptions.none()),
            RateLimitExceededException.class);

    assertThat(exception.statusCode()).isEqualTo(429);
    assertThat(exception.retryAfterSeconds()).isEqualTo(60);
  }

  @Test
  void shouldThrowRateLimitExceptionOn429WithoutRetryAfterHeader() {
    // Given
    stubFor(
        post(urlEqualTo("/events/send"))
            .willReturn(aResponse().withStatus(429).withBody(TestFixtures.rateLimitErrorResponse())));

    // When/Then
    RateLimitExceededException exception =
        catchThrowableOfType(
            () ->
                coreSender.postJson(
                    "/events/send",
                    TestFixtures.minimalEventSendRequest(),
                    ContactResponse.class,
                    RequestOptions.none()),
            RateLimitExceededException.class);

    assertThat(exception.statusCode()).isEqualTo(429);
    assertThat(exception.retryAfterSeconds()).isEqualTo(0);
  }

  @Test
  void shouldThrowLoopsApiExceptionOn400() {
    // Given
    stubFor(
        post(urlEqualTo("/contacts/create"))
            .willReturn(
                aResponse().withStatus(400).withBody(TestFixtures.validationErrorResponse())));

    // When/Then
    LoopsApiException exception =
        catchThrowableOfType(
            () ->
                coreSender.postJson(
                    "/contacts/create",
                    TestFixtures.minimalContactCreateRequest(),
                    ContactResponse.class,
                    RequestOptions.none()),
            LoopsApiException.class);

    assertThat(exception.statusCode()).isEqualTo(400);
    assertThat(exception.rawBody()).contains("Invalid email format");
  }

  @Test
  void shouldThrowLoopsApiExceptionOn401() {
    // Given
    stubFor(
        post(urlEqualTo("/contacts/create"))
            .willReturn(
                aResponse().withStatus(401).withBody(TestFixtures.unauthorizedErrorResponse())));

    // When/Then
    LoopsApiException exception =
        catchThrowableOfType(
            () ->
                coreSender.postJson(
                    "/contacts/create",
                    TestFixtures.minimalContactCreateRequest(),
                    ContactResponse.class,
                    RequestOptions.none()),
            LoopsApiException.class);

    assertThat(exception.statusCode()).isEqualTo(401);
    assertThat(exception.rawBody()).contains("Invalid API key");
  }

  @Test
  void shouldThrowLoopsApiExceptionOn500() {
    // Given
    stubFor(
        post(urlEqualTo("/contacts/create"))
            .willReturn(
                aResponse().withStatus(500).withBody(TestFixtures.serverErrorResponse())));

    // When/Then
    LoopsApiException exception =
        catchThrowableOfType(
            () ->
                coreSender.postJson(
                    "/contacts/create",
                    TestFixtures.minimalContactCreateRequest(),
                    ContactResponse.class,
                    RequestOptions.none()),
            LoopsApiException.class);

    assertThat(exception.statusCode()).isEqualTo(500);
    assertThat(exception.rawBody()).contains("Internal server error");
  }

  @Test
  void shouldHandleAsyncPostRequestSuccessfully() {
    // Given
    stubFor(
        post(urlEqualTo("/contacts/create"))
            .willReturn(okJson("""
                {"id": "contact-123", "message": "Success"}
                """)));

    // When
    CompletableFuture<ContactResponse> future =
        coreSender.postJsonAsync(
            "/contacts/create",
            TestFixtures.minimalContactCreateRequest(),
            ContactResponse.class,
            RequestOptions.none());

    // Then
    ContactResponse response = future.join();
    assertThat(response).isNotNull();
    assertThat(response.id()).isNotNull();
  }

  @Test
  void shouldHandleAsyncPostRequestFailure() {
    // Given
    stubFor(
        post(urlEqualTo("/contacts/create"))
            .willReturn(
                aResponse().withStatus(400).withBody(TestFixtures.validationErrorResponse())));

    // When
    CompletableFuture<ContactResponse> future =
        coreSender.postJsonAsync(
            "/contacts/create",
            TestFixtures.minimalContactCreateRequest(),
            ContactResponse.class,
            RequestOptions.none());

    // Then
    assertThatThrownBy(future::join)
        .hasCauseInstanceOf(LoopsApiException.class)
        .cause()
        .hasFieldOrPropertyWithValue("statusCode", 400);
  }

  @Test
  void shouldDeserializeResponseCorrectly() {
    // Given
    String jsonResponse =
        """
        {
          "id": "contact-xyz",
          "message": "Success"
        }
        """;
    stubFor(post(urlEqualTo("/test")).willReturn(okJson(jsonResponse)));

    // When
    ContactResponse response =
        coreSender.postJson(
            "/test",
            TestFixtures.minimalContactCreateRequest(),
            ContactResponse.class,
            RequestOptions.none());

    // Then
    assertThat(response.id()).isNotNull();
    assertThat(response.id()).isEqualTo("contact-xyz");
    assertThat(response.message()).isEqualTo("Success");
  }

  @Test
  void shouldUrlEncodeQueryParameters() {
    // Given
    stubFor(
        get(urlPathEqualTo("/search"))
            .withQueryParam("query", equalTo("test value"))
            .willReturn(okJson("""
                {"id": "contact-123", "message": "Success"}
                """)));

    // When
    coreSender.get(
        "/search",
        Map.of("query", "test value"),
        ContactResponse.class,
        RequestOptions.none());

    // Then
    verify(
        getRequestedFor(urlPathEqualTo("/search"))
            .withQueryParam("query", equalTo("test value")));
  }

  @Test
  void shouldHandlePutRequest() {
    // Given
    stubFor(
        put(urlEqualTo("/contacts/update"))
            .willReturn(okJson("""
                {"id": "contact-123", "message": "Success"}
                """)));

    // When
    ContactResponse response =
        coreSender.putJson(
            "/contacts/update",
            TestFixtures.minimalContactCreateRequest(),
            ContactResponse.class,
            RequestOptions.none());

    // Then
    assertThat(response.id()).isNotNull();
    verify(
        putRequestedFor(urlEqualTo("/contacts/update"))
            .withHeader("Authorization", equalTo("Bearer " + TestFixtures.TEST_API_KEY)));
  }

  @Test
  void shouldHandleDeleteRequest() {
    // Given
    stubFor(
        delete(urlEqualTo("/contacts/delete"))
            .willReturn(okJson("""
                {"id": "contact-123", "message": "Success"}
                """)));

    // When
    ContactResponse response =
        coreSender.deleteJson(
            "/contacts/delete",
            Map.of("email", "user@example.com"),
            ContactResponse.class,
            RequestOptions.none());

    // Then
    assertThat(response.id()).isNotNull();
    verify(
        deleteRequestedFor(urlEqualTo("/contacts/delete"))
            .withHeader("Authorization", equalTo("Bearer " + TestFixtures.TEST_API_KEY)));
  }
}
