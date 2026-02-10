package com.telos.loops.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.telos.loops.AsyncTestUtils;
import com.telos.loops.LoopsClient;
import com.telos.loops.TestFixtures;
import com.telos.loops.WireMockSetup;
import com.telos.loops.apikey.ApiKeyTestResponse;
import com.telos.loops.contacts.ContactResponse;
import com.telos.loops.events.EventResponse;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoopsClientIT {

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
  void shouldPerformEndToEndWorkflowSuccessfully() {
    // Given: Mock all API endpoints
    wireMock.stubGetSuccess("/api-key", TestFixtures.apiKeyTestSuccessResponse());
    wireMock.stubPostSuccess("/contacts/create", TestFixtures.contactCreateSuccessResponse());
    wireMock.stubPostSuccess("/events/send", TestFixtures.eventSendSuccessResponse());

    // When: Test API key
    ApiKeyTestResponse apiKeyResponse = client.apiKey().test();

    // Then
    assertThat(apiKeyResponse.success()).isTrue();

    // When: Create contact
    ContactResponse contactResponse = client.contacts().create(
        TestFixtures.fullContactCreateRequest());

    // Then
    assertThat(contactResponse.id()).isEqualTo("contact-123");

    // When: Send event
    EventResponse eventResponse = client.events().send(
        TestFixtures.eventSendRequestWithProperties());

    // Then
    assertThat(eventResponse.success()).isTrue();

    // Verify all endpoints were called
    verify(getRequestedFor(urlEqualTo("/api-key")));
    verify(postRequestedFor(urlEqualTo("/contacts/create")));
    verify(postRequestedFor(urlEqualTo("/events/send")));
  }

  @Test
  void shouldPerformAsyncWorkflowSuccessfully() {
    // Given
    wireMock.stubPostSuccess("/contacts/create", TestFixtures.contactCreateSuccessResponse());
    wireMock.stubPostSuccess("/events/send", TestFixtures.eventSendSuccessResponse());

    // When: Run async operations
    CompletableFuture<ContactResponse> contactFuture =
        client.contacts().createAsync(TestFixtures.minimalContactCreateRequest());

    CompletableFuture<EventResponse> eventFuture =
        client.events().sendAsync(TestFixtures.minimalEventSendRequest());

    // Then: Wait for both to complete
    ContactResponse contactResponse = AsyncTestUtils.awaitCompletion(contactFuture);
    EventResponse eventResponse = AsyncTestUtils.awaitCompletion(eventFuture);

    assertThat(contactResponse).isNotNull();
    assertThat(eventResponse).isNotNull();
    assertThat(eventResponse.success()).isTrue();
  }

  @Test
  void shouldInitializeAllClientsCorrectly() {
    // When/Then: Verify all clients are accessible
    assertThat(client.contacts()).isNotNull();
    assertThat(client.events()).isNotNull();
    assertThat(client.apiKey()).isNotNull();
    assertThat(client.transactional()).isNotNull();
    assertThat(client.mailingLists()).isNotNull();
    assertThat(client.contactProperties()).isNotNull();
    assertThat(client.dedicatedIps()).isNotNull();
  }

  @Test
  void shouldFailToCreateClientWithoutApiKey() {
    // When/Then
    assertThatThrownBy(() -> LoopsClient.builder().build())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("API key is required");
  }

  @Test
  void shouldFailToCreateClientWithBlankApiKey() {
    // When/Then
    assertThatThrownBy(() -> LoopsClient.builder().apiKey("  ").build())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("API key is required");
  }

  @Test
  void shouldAllowCustomBaseUrl() {
    // When
    LoopsClient customClient = LoopsClient.builder()
        .apiKey("test-key")
        .baseUrl("https://custom.api.com")
        .build();

    // Then
    assertThat(customClient).isNotNull();
  }
}
