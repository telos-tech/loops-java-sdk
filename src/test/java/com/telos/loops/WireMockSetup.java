package com.telos.loops;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.telos.loops.transport.OkHttpTransport;
import com.telos.loops.transport.Transport;

/** Utility class for setting up WireMock HTTP server in tests. */
public class WireMockSetup {

  private final WireMockServer wireMockServer;
  private final String baseUrl;

  /** Creates and starts a WireMock server on a random port. */
  public WireMockSetup() {
    this.wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
    this.wireMockServer.start();
    this.baseUrl = "http://localhost:" + wireMockServer.port();
    WireMock.configureFor("localhost", wireMockServer.port());
  }

  /** Returns the base URL of the WireMock server. */
  public String getBaseUrl() {
    return baseUrl;
  }

  /** Returns the WireMock server instance for stubbing. */
  public WireMockServer getServer() {
    return wireMockServer;
  }

  /** Creates a LoopsClient configured to use this WireMock server. */
  public LoopsClient createClient(String apiKey) {
    return LoopsClient.builder().apiKey(apiKey).baseUrl(baseUrl).build();
  }

  /** Resets all stubs and request history. */
  public void reset() {
    wireMockServer.resetAll();
  }

  /** Stops the WireMock server. */
  public void stop() {
    wireMockServer.stop();
  }

  /** Stub a successful POST request returning JSON. */
  public void stubPostSuccess(String path, String responseBody) {
    stubFor(post(urlEqualTo(path)).willReturn(okJson(responseBody)));
  }

  /** Stub a successful GET request returning JSON. */
  public void stubGetSuccess(String path, String responseBody) {
    stubFor(get(urlEqualTo(path)).willReturn(okJson(responseBody)));
  }

  /** Stub a rate limit error (429) with retry-after header. */
  public void stubRateLimit(String path, int retryAfterSeconds) {
    stubFor(
        post(urlEqualTo(path))
            .willReturn(
                aResponse()
                    .withStatus(429)
                    .withHeader("Retry-After", String.valueOf(retryAfterSeconds))
                    .withBody(TestFixtures.rateLimitErrorResponse())));
  }

  /** Stub an unauthorized error (401). */
  public void stubUnauthorized(String path) {
    stubFor(
        post(urlEqualTo(path))
            .willReturn(
                aResponse()
                    .withStatus(401)
                    .withBody(TestFixtures.unauthorizedErrorResponse())));
  }

  /** Stub a validation error (400). */
  public void stubValidationError(String path) {
    stubFor(
        post(urlEqualTo(path))
            .willReturn(
                aResponse()
                    .withStatus(400)
                    .withBody(TestFixtures.validationErrorResponse())));
  }

  /** Stub a server error (500). */
  public void stubServerError(String path) {
    stubFor(
        post(urlEqualTo(path))
            .willReturn(
                aResponse().withStatus(500).withBody(TestFixtures.serverErrorResponse())));
  }

  /** Verify that a request was made with the expected authorization header. */
  public void verifyAuthorizationHeader(String expectedApiKey) {
    verify(
        postRequestedFor(anyUrl())
            .withHeader("Authorization", equalTo("Bearer " + expectedApiKey)));
  }

  /** Verify that a specific path was called. */
  public void verifyPath(String path) {
    verify(postRequestedFor(urlEqualTo(path)));
  }
}
