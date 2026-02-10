package com.telos.loops.properties;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.telos.loops.AsyncTestUtils;
import com.telos.loops.LoopsClient;
import com.telos.loops.TestFixtures;
import com.telos.loops.WireMockSetup;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactPropertiesClientTest {

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
  void shouldListContactPropertiesSuccessfully() {
    // Given
    wireMock.stubGetSuccess("/contacts/properties", TestFixtures.contactPropertiesResponse());

    // When
    List<ContactProperty> properties = client.contactProperties().list();

    // Then
    assertThat(properties).isNotEmpty();
    assertThat(properties).hasSize(2);
    assertThat(properties.get(0).key()).isEqualTo("firstName");
    assertThat(properties.get(0).type()).isEqualTo("string");
  }

  @Test
  void shouldListContactPropertiesAsyncSuccessfully() {
    // Given
    wireMock.stubGetSuccess("/contacts/properties", TestFixtures.contactPropertiesResponse());

    // When
    CompletableFuture<List<ContactProperty>> future = client.contactProperties().listAsync();

    // Then
    List<ContactProperty> properties = AsyncTestUtils.awaitCompletion(future);
    assertThat(properties).isNotEmpty();
  }

  @Test
  void shouldCreateContactPropertySuccessfully() {
    // Given
    wireMock.stubPostSuccess("/contacts/properties", TestFixtures.contactPropertyCreateResponse());

    // When
    ContactPropertyResponse response = client.contactProperties().create(
        TestFixtures.contactPropertyCreateRequest());

    // Then
    assertThat(response).isNotNull();
  }

  @Test
  void shouldCreateContactPropertyAsyncSuccessfully() {
    // Given
    wireMock.stubPostSuccess("/contacts/properties", TestFixtures.contactPropertyCreateResponse());

    // When
    CompletableFuture<ContactPropertyResponse> future = client.contactProperties().createAsync(
        TestFixtures.contactPropertyCreateRequest());

    // Then
    ContactPropertyResponse response = AsyncTestUtils.awaitCompletion(future);
    assertThat(response).isNotNull();
  }
}
