package com.telos.loops.internal.mappers;

import static org.assertj.core.api.Assertions.*;

import com.telos.loops.contacts.Contact;
import com.telos.loops.contacts.ContactCreateRequest;
import com.telos.loops.contacts.ContactDeleteRequest;
import com.telos.loops.contacts.ContactFindRequest;
import com.telos.loops.contacts.ContactResponse;
import com.telos.loops.contacts.ContactUpdateRequest;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ContactsMapperTest {

  @Test
  void shouldMapContactCreateRequestToGenerated() {
    // Given
    ContactCreateRequest request = new ContactCreateRequest(
        "test@example.com",
        "John",
        "Doe",
        true,
        "premium",
        "user-123",
        Map.of("newsletter", true),
        Map.of("customField", "value"));

    // When
    var generated = ContactsMapper.toGenerated(request);

    // Then
    assertThat(generated).isNotNull();
    assertThat(generated.getEmail()).isEqualTo("test@example.com");
    assertThat(generated.getFirstName()).isEqualTo("John");
    assertThat(generated.getLastName()).isEqualTo("Doe");
    assertThat(generated.getSubscribed()).isTrue();
  }

  @Test
  void shouldMapContactUpdateRequestToGenerated() {
    // Given
    ContactUpdateRequest request = new ContactUpdateRequest(
        "test@example.com",
        "Jane",
        "Smith",
        false,
        "basic",
        "user-456",
        Map.of("updates", false),
        Map.of("field", "data"));

    // When
    var generated = ContactsMapper.toGenerated(request);

    // Then
    assertThat(generated).isNotNull();
    assertThat(generated.getEmail()).isEqualTo("test@example.com");
    assertThat(generated.getFirstName()).isEqualTo("Jane");
  }

  @Test
  void shouldMapContactFindRequestToQueryParams() {
    // Given
    ContactFindRequest request = new ContactFindRequest("test@example.com", null);

    // When
    Map<String, String> queryParams = ContactsMapper.toQueryParams(request);

    // Then
    assertThat(queryParams).containsEntry("email", "test@example.com");
    assertThat(queryParams).doesNotContainKey("userId");
  }

  @Test
  void shouldMapContactFindRequestWithUserIdToQueryParams() {
    // Given
    ContactFindRequest request = new ContactFindRequest(null, "user-123");

    // When
    Map<String, String> queryParams = ContactsMapper.toQueryParams(request);

    // Then
    assertThat(queryParams).containsEntry("userId", "user-123");
    assertThat(queryParams).doesNotContainKey("email");
  }

  @Test
  void shouldMapContactDeleteRequestToGenerated() {
    // Given
    ContactDeleteRequest request = new ContactDeleteRequest("test@example.com", null);

    // When
    var generated = ContactsMapper.toGenerated(request);

    // Then
    assertThat(generated).isNotNull();
    assertThat(generated.getEmail()).isEqualTo("test@example.com");
  }

  @Test
  void shouldHandleNullValuesInRequests() {
    // Given
    ContactCreateRequest request = new ContactCreateRequest(
        "test@example.com", null, null, false, null, null, null, null);

    // When
    var generated = ContactsMapper.toGenerated(request);

    // Then
    assertThat(generated).isNotNull();
    assertThat(generated.getEmail()).isEqualTo("test@example.com");
    assertThat(generated.getFirstName()).isNull();
  }
}
