package com.telos.loops.contacts;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.telos.loops.AsyncTestUtils;
import com.telos.loops.LoopsClient;
import com.telos.loops.TestFixtures;
import com.telos.loops.WireMockSetup;
import com.telos.loops.error.LoopsApiException;
import com.telos.loops.error.RateLimitExceededException;
import com.telos.loops.model.RequestOptions;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactsClientTest {

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
  // Create Operation Tests
  // ============================================================

  @Test
  void shouldCreateContactSuccessfully() {
    // Given
    wireMock.stubPostSuccess("/contacts/create", TestFixtures.contactCreateSuccessResponse());

    // When
    ContactResponse response = client.contacts().create(TestFixtures.minimalContactCreateRequest());

    // Then
    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo("contact-123");
    verify(
        postRequestedFor(urlEqualTo("/contacts/create"))
            .withHeader("Authorization", equalTo("Bearer " + TestFixtures.TEST_API_KEY))
            .withHeader("Content-Type", equalTo("application/json")));
  }

  @Test
  void shouldCreateContactWithFullData() {
    // Given
    wireMock.stubPostSuccess("/contacts/create", TestFixtures.contactCreateSuccessResponse());

    // When
    ContactResponse response = client.contacts().create(TestFixtures.fullContactCreateRequest());

    // Then
    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo("contact-123");
    verify(postRequestedFor(urlEqualTo("/contacts/create")));
  }

  @Test
  void shouldCreateContactAsyncSuccessfully() {
    // Given
    wireMock.stubPostSuccess("/contacts/create", TestFixtures.contactCreateSuccessResponse());

    // When
    CompletableFuture<ContactResponse> future =
        client.contacts().createAsync(TestFixtures.minimalContactCreateRequest());

    // Then
    ContactResponse response = AsyncTestUtils.awaitCompletion(future);
    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo("contact-123");
  }

  @Test
  void shouldCreateContactWithRequestOptions() {
    // Given
    wireMock.stubPostSuccess("/contacts/create", TestFixtures.contactCreateSuccessResponse());
    RequestOptions options =
        new RequestOptions(Map.of("X-Custom-Header", "custom-value", "X-Request-Id", "req-123"));

    // When
    ContactResponse response =
        client.contacts().create(TestFixtures.minimalContactCreateRequest(), options);

    // Then
    assertThat(response).isNotNull();
    verify(
        postRequestedFor(urlEqualTo("/contacts/create"))
            .withHeader("X-Custom-Header", equalTo("custom-value"))
            .withHeader("X-Request-Id", equalTo("req-123")));
  }

  // ============================================================
  // Update Operation Tests
  // ============================================================

  @Test
  void shouldUpdateContactSuccessfully() {
    // Given
    stubFor(put(urlEqualTo("/contacts/update"))
        .willReturn(okJson(TestFixtures.contactCreateSuccessResponse())));
    ContactUpdateRequest updateRequest =
        new ContactUpdateRequest(
            TestFixtures.TEST_EMAIL, "Jane", "Smith", null, null, null, null, null);

    // When
    ContactResponse response = client.contacts().update(updateRequest);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo("contact-123");
    verify(
        putRequestedFor(urlEqualTo("/contacts/update"))
            .withHeader("Authorization", equalTo("Bearer " + TestFixtures.TEST_API_KEY)));
  }

  @Test
  void shouldUpdateContactAsyncSuccessfully() {
    // Given
    stubFor(put(urlEqualTo("/contacts/update"))
        .willReturn(okJson(TestFixtures.contactCreateSuccessResponse())));
    ContactUpdateRequest updateRequest =
        new ContactUpdateRequest(
            TestFixtures.TEST_EMAIL, "Jane", "Smith", null, null, null, null, null);

    // When
    CompletableFuture<ContactResponse> future = client.contacts().updateAsync(updateRequest);

    // Then
    ContactResponse response = AsyncTestUtils.awaitCompletion(future);
    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo("contact-123");
  }

  // ============================================================
  // Find Operation Tests
  // ============================================================

  @Test
  void shouldFindContactsByEmail() {
    // Given
    String responseBody =
        """
        [
          {
            "id": "contact-123",
            "email": "user@example.com"
          }
        ]
        """;
    stubFor(
        get(urlPathEqualTo("/contacts/find"))
            .withQueryParam("email", equalTo(TestFixtures.TEST_EMAIL))
            .willReturn(okJson(responseBody)));
    ContactFindRequest findRequest = new ContactFindRequest(TestFixtures.TEST_EMAIL, null);

    // When
    List<Contact> contacts = client.contacts().find(findRequest);

    // Then
    assertThat(contacts).isNotEmpty();
    assertThat(contacts).hasSize(1);
    assertThat(contacts.get(0).email()).isEqualTo("user@example.com");
    assertThat(contacts.get(0).id()).isEqualTo("contact-123");
    verify(
        getRequestedFor(urlPathEqualTo("/contacts/find"))
            .withQueryParam("email", equalTo(TestFixtures.TEST_EMAIL)));
  }

  @Test
  void shouldFindContactsByUserId() {
    // Given
    String responseBody =
        """
        [
          {
            "id": "contact-123",
            "email": "user@example.com"
          }
        ]
        """;
    stubFor(
        get(urlPathEqualTo("/contacts/find"))
            .withQueryParam("userId", equalTo(TestFixtures.TEST_USER_ID))
            .willReturn(okJson(responseBody)));
    ContactFindRequest findRequest = new ContactFindRequest(null, TestFixtures.TEST_USER_ID);

    // When
    List<Contact> contacts = client.contacts().find(findRequest);

    // Then
    assertThat(contacts).isNotEmpty();
    assertThat(contacts).hasSize(1);
    assertThat(contacts.get(0).id()).isEqualTo("contact-123");
    verify(
        getRequestedFor(urlPathEqualTo("/contacts/find"))
            .withQueryParam("userId", equalTo(TestFixtures.TEST_USER_ID)));
  }

  @Test
  void shouldFindContactsAsyncSuccessfully() {
    // Given
    String responseBody =
        """
        [
          {
            "id": "contact-123",
            "email": "user@example.com"
          }
        ]
        """;
    stubFor(
        get(urlPathEqualTo("/contacts/find"))
            .withQueryParam("email", equalTo(TestFixtures.TEST_EMAIL))
            .willReturn(okJson(responseBody)));
    ContactFindRequest findRequest = new ContactFindRequest(TestFixtures.TEST_EMAIL, null);

    // When
    CompletableFuture<List<Contact>> future = client.contacts().findAsync(findRequest);

    // Then
    List<Contact> contacts = AsyncTestUtils.awaitCompletion(future);
    assertThat(contacts).isNotEmpty();
    assertThat(contacts).hasSize(1);
  }

  @Test
  void shouldReturnEmptyListWhenNoContactsFound() {
    // Given
    stubFor(
        get(urlPathEqualTo("/contacts/find"))
            .withQueryParam("email", equalTo("notfound@example.com"))
            .willReturn(okJson("[]")));
    ContactFindRequest findRequest = new ContactFindRequest("notfound@example.com", null);

    // When
    List<Contact> contacts = client.contacts().find(findRequest);

    // Then
    assertThat(contacts).isEmpty();
  }

  // ============================================================
  // Delete Operation Tests
  // ============================================================

  @Test
  void shouldDeleteContactByEmail() {
    // Given
    stubFor(delete(urlEqualTo("/contacts/delete"))
        .willReturn(okJson("""
            {"message": "Contact deleted successfully"}
            """)));
    ContactDeleteRequest deleteRequest = new ContactDeleteRequest(TestFixtures.TEST_EMAIL, null);

    // When
    ContactResponse response = client.contacts().delete(deleteRequest);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.message()).isEqualTo("Contact deleted successfully");
    verify(
        deleteRequestedFor(urlEqualTo("/contacts/delete"))
            .withHeader("Authorization", equalTo("Bearer " + TestFixtures.TEST_API_KEY)));
  }

  @Test
  void shouldDeleteContactByUserId() {
    // Given
    stubFor(delete(urlEqualTo("/contacts/delete"))
        .willReturn(okJson("""
            {"message": "Contact deleted successfully"}
            """)));
    ContactDeleteRequest deleteRequest = new ContactDeleteRequest(null, TestFixtures.TEST_USER_ID);

    // When
    ContactResponse response = client.contacts().delete(deleteRequest);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.message()).isEqualTo("Contact deleted successfully");
  }

  @Test
  void shouldDeleteContactAsyncSuccessfully() {
    // Given
    stubFor(delete(urlEqualTo("/contacts/delete"))
        .willReturn(okJson("""
            {"message": "Contact deleted successfully"}
            """)));
    ContactDeleteRequest deleteRequest = new ContactDeleteRequest(TestFixtures.TEST_EMAIL, null);

    // When
    CompletableFuture<ContactResponse> future = client.contacts().deleteAsync(deleteRequest);

    // Then
    ContactResponse response = AsyncTestUtils.awaitCompletion(future);
    assertThat(response).isNotNull();
    assertThat(response.message()).isEqualTo("Contact deleted successfully");
  }

  // ============================================================
  // Error Handling Tests
  // ============================================================

  @Test
  void shouldThrowLoopsApiExceptionOn400ValidationError() {
    // Given
    wireMock.stubValidationError("/contacts/create");

    // When/Then
    LoopsApiException exception =
        catchThrowableOfType(
            () -> client.contacts().create(TestFixtures.minimalContactCreateRequest()),
            LoopsApiException.class);

    assertThat(exception.statusCode()).isEqualTo(400);
    assertThat(exception.rawBody()).contains("Invalid email format");
  }

  @Test
  void shouldThrowLoopsApiExceptionOn401Unauthorized() {
    // Given
    wireMock.stubUnauthorized("/contacts/create");

    // When/Then
    LoopsApiException exception =
        catchThrowableOfType(
            () -> client.contacts().create(TestFixtures.minimalContactCreateRequest()),
            LoopsApiException.class);

    assertThat(exception.statusCode()).isEqualTo(401);
    assertThat(exception.rawBody()).contains("Invalid API key");
  }

  @Test
  void shouldThrowRateLimitExceptionOn429WithRetryAfter() {
    // Given
    wireMock.stubRateLimit("/contacts/create", 60);

    // When/Then
    RateLimitExceededException exception =
        catchThrowableOfType(
            () -> client.contacts().create(TestFixtures.minimalContactCreateRequest()),
            RateLimitExceededException.class);

    assertThat(exception.statusCode()).isEqualTo(429);
    assertThat(exception.retryAfterSeconds()).isEqualTo(60);
  }

  @Test
  void shouldThrowRateLimitExceptionOn429WithoutRetryAfter() {
    // Given
    stubFor(
        post(urlEqualTo("/contacts/create"))
            .willReturn(
                aResponse().withStatus(429).withBody(TestFixtures.rateLimitErrorResponse())));

    // When/Then
    RateLimitExceededException exception =
        catchThrowableOfType(
            () -> client.contacts().create(TestFixtures.minimalContactCreateRequest()),
            RateLimitExceededException.class);

    assertThat(exception.statusCode()).isEqualTo(429);
    assertThat(exception.retryAfterSeconds()).isEqualTo(0);
  }

  @Test
  void shouldThrowLoopsApiExceptionOn500ServerError() {
    // Given
    wireMock.stubServerError("/contacts/create");

    // When/Then
    LoopsApiException exception =
        catchThrowableOfType(
            () -> client.contacts().create(TestFixtures.minimalContactCreateRequest()),
            LoopsApiException.class);

    assertThat(exception.statusCode()).isEqualTo(500);
    assertThat(exception.rawBody()).contains("Internal server error");
  }

  @Test
  void shouldPropagateErrorInAsyncOperation() {
    // Given
    wireMock.stubValidationError("/contacts/create");

    // When
    CompletableFuture<ContactResponse> future =
        client.contacts().createAsync(TestFixtures.minimalContactCreateRequest());

    // Then
    LoopsApiException exception = AsyncTestUtils.awaitException(future, LoopsApiException.class);
    assertThat(exception.statusCode()).isEqualTo(400);
  }

  @Test
  void shouldPropagateRateLimitErrorInAsyncOperation() {
    // Given
    wireMock.stubRateLimit("/contacts/create", 30);

    // When
    CompletableFuture<ContactResponse> future =
        client.contacts().createAsync(TestFixtures.minimalContactCreateRequest());

    // Then
    RateLimitExceededException exception =
        AsyncTestUtils.awaitException(future, RateLimitExceededException.class);
    assertThat(exception.statusCode()).isEqualTo(429);
    assertThat(exception.retryAfterSeconds()).isEqualTo(30);
  }
}
