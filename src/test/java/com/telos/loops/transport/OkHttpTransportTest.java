package com.telos.loops.transport;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OkHttpTransportTest {

  private WireMockServer wireMockServer;
  private OkHttpTransport transport;
  private String baseUrl;

  @BeforeEach
  void setUp() {
    wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
    wireMockServer.start();
    baseUrl = "http://localhost:" + wireMockServer.port();
    configureFor("localhost", wireMockServer.port());

    transport = new OkHttpTransport();
  }

  @AfterEach
  void tearDown() {
    wireMockServer.stop();
  }

  @Test
  void shouldExecuteGetRequestSuccessfully() {
    // Given
    stubFor(get(urlEqualTo("/test")).willReturn(okJson("{\"status\": \"ok\"}")));

    TransportRequest request =
        new TransportRequest(
            HttpMethod.GET, baseUrl + "/test", Map.of("Authorization", "Bearer test"), new byte[0]);

    // When
    TransportResponse response = transport.execute(request);

    // Then
    assertThat(response.status()).isEqualTo(200);
    String body = new String(response.response(), StandardCharsets.UTF_8);
    assertThat(body).contains("\"status\": \"ok\"");
  }

  @Test
  void shouldExecutePostRequestWithBody() {
    // Given
    stubFor(
        post(urlEqualTo("/create"))
            .withRequestBody(equalToJson("{\"name\": \"test\"}"))
            .willReturn(okJson("{\"id\": \"123\"}")));

    byte[] requestBody = "{\"name\": \"test\"}".getBytes(StandardCharsets.UTF_8);
    TransportRequest request =
        new TransportRequest(
            HttpMethod.POST,
            baseUrl + "/create",
            Map.of("Content-Type", "application/json", "Authorization", "Bearer test"),
            requestBody);

    // When
    TransportResponse response = transport.execute(request);

    // Then
    assertThat(response.status()).isEqualTo(200);
    String body = new String(response.response(), StandardCharsets.UTF_8);
    assertThat(body).contains("\"id\": \"123\"");
  }

  @Test
  void shouldPropagateHeadersCorrectly() {
    // Given
    stubFor(
        post(urlEqualTo("/test"))
            .withHeader("Authorization", equalTo("Bearer secret"))
            .withHeader("X-Custom", equalTo("value"))
            .willReturn(ok()));

    TransportRequest request =
        new TransportRequest(
            HttpMethod.POST,
            baseUrl + "/test",
            Map.of("Authorization", "Bearer secret", "X-Custom", "value"),
            new byte[0]);

    // When
    transport.execute(request);

    // Then
    verify(
        postRequestedFor(urlEqualTo("/test"))
            .withHeader("Authorization", equalTo("Bearer secret"))
            .withHeader("X-Custom", equalTo("value")));
  }

  @Test
  void shouldHandleErrorStatusCodes() {
    // Given
    stubFor(post(urlEqualTo("/error")).willReturn(aResponse().withStatus(400).withBody("Bad Request")));

    TransportRequest request =
        new TransportRequest(
            HttpMethod.POST, baseUrl + "/error", Map.of("Authorization", "Bearer test"), new byte[0]);

    // When
    TransportResponse response = transport.execute(request);

    // Then
    assertThat(response.status()).isEqualTo(400);
    String body = new String(response.response(), StandardCharsets.UTF_8);
    assertThat(body).isEqualTo("Bad Request");
  }

  @Test
  void shouldCaptureResponseHeaders() {
    // Given
    stubFor(
        get(urlEqualTo("/headers"))
            .willReturn(
                ok().withHeader("X-Response-Id", "abc123")
                    .withHeader("Retry-After", "60")
                    .withBody("{}")));

    TransportRequest request =
        new TransportRequest(
            HttpMethod.GET, baseUrl + "/headers", Map.of(), new byte[0]);

    // When
    TransportResponse response = transport.execute(request);

    // Then
    assertThat(response.headers()).containsEntry("X-Response-Id", "abc123");
    assertThat(response.headers()).containsEntry("Retry-After", "60");
  }

  @Test
  void shouldExecuteAsyncRequestSuccessfully() {
    // Given
    stubFor(get(urlEqualTo("/async")).willReturn(okJson("{\"async\": true}")));

    TransportRequest request =
        new TransportRequest(
            HttpMethod.GET, baseUrl + "/async", Map.of("Authorization", "Bearer test"), new byte[0]);

    // When
    CompletableFuture<TransportResponse> future = transport.executeAsync(request);

    // Then
    TransportResponse response = future.join();
    assertThat(response.status()).isEqualTo(200);
    String body = new String(response.response(), StandardCharsets.UTF_8);
    assertThat(body).contains("\"async\": true");
  }

  @Test
  void shouldHandleAsyncRequestFailure() {
    // Given - Configure WireMock to close connection
    stubFor(get(urlEqualTo("/fail")).willReturn(aResponse().withFault(com.github.tomakehurst.wiremock.http.Fault.CONNECTION_RESET_BY_PEER)));

    TransportRequest request =
        new TransportRequest(
            HttpMethod.GET, baseUrl + "/fail", Map.of(), new byte[0]);

    // When
    CompletableFuture<TransportResponse> future = transport.executeAsync(request);

    // Then
    assertThatThrownBy(future::join).hasCauseInstanceOf(java.io.IOException.class);
  }

  @Test
  void shouldHandlePutRequest() {
    // Given
    stubFor(put(urlEqualTo("/update")).willReturn(ok()));

    byte[] requestBody = "{\"update\": true}".getBytes(StandardCharsets.UTF_8);
    TransportRequest request =
        new TransportRequest(
            HttpMethod.PUT,
            baseUrl + "/update",
            Map.of("Content-Type", "application/json"),
            requestBody);

    // When
    TransportResponse response = transport.execute(request);

    // Then
    assertThat(response.status()).isEqualTo(200);
    verify(putRequestedFor(urlEqualTo("/update")).withRequestBody(equalToJson("{\"update\": true}")));
  }

  @Test
  void shouldHandleDeleteRequest() {
    // Given
    stubFor(delete(urlEqualTo("/delete")).willReturn(ok()));

    TransportRequest request =
        new TransportRequest(
            HttpMethod.DELETE, baseUrl + "/delete", Map.of(), new byte[0]);

    // When
    TransportResponse response = transport.execute(request);

    // Then
    assertThat(response.status()).isEqualTo(200);
    verify(deleteRequestedFor(urlEqualTo("/delete")));
  }

  @Test
  void shouldHandleDeleteRequestWithBody() {
    // Given
    stubFor(delete(urlEqualTo("/delete")).willReturn(ok()));

    byte[] requestBody = "{\"id\": \"123\"}".getBytes(StandardCharsets.UTF_8);
    TransportRequest request =
        new TransportRequest(
            HttpMethod.DELETE,
            baseUrl + "/delete",
            Map.of("Content-Type", "application/json"),
            requestBody);

    // When
    TransportResponse response = transport.execute(request);

    // Then
    assertThat(response.status()).isEqualTo(200);
    verify(deleteRequestedFor(urlEqualTo("/delete")).withRequestBody(equalToJson("{\"id\": \"123\"}")));
  }

  @Test
  void shouldHandleEmptyResponseBody() {
    // Given
    stubFor(post(urlEqualTo("/empty")).willReturn(ok().withBody("")));

    TransportRequest request =
        new TransportRequest(
            HttpMethod.POST, baseUrl + "/empty", Map.of(), new byte[0]);

    // When
    TransportResponse response = transport.execute(request);

    // Then
    assertThat(response.status()).isEqualTo(200);
    assertThat(response.response()).isEmpty();
  }
}
