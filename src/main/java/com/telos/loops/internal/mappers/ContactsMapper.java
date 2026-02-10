package com.telos.loops.internal.mappers;

import com.telos.loops.contacts.*;
import java.util.HashMap;
import java.util.Map;

public class ContactsMapper {

  // ============================================================
  // Request Mappings: Public API → Generated OpenAPI Models
  // ============================================================

  public static com.telos.loops.internal.openapi.model.ContactRequest toGenerated(
      ContactCreateRequest request) {
    var generated = new com.telos.loops.internal.openapi.model.ContactRequest();
    generated.setEmail(request.email());
    generated.setFirstName(request.firstName());
    generated.setLastName(request.lastName());
    generated.setSubscribed(request.subscribed());
    generated.setUserGroup(request.userGroup());
    generated.setUserId(request.userId());
    generated.setMailingLists(request.mailingLists());
    return generated;
  }

  public static com.telos.loops.internal.openapi.model.ContactUpdateRequest toGenerated(
      ContactUpdateRequest request) {
    var generated = new com.telos.loops.internal.openapi.model.ContactUpdateRequest();
    generated.setEmail(request.email());
    generated.setFirstName(request.firstName());
    generated.setLastName(request.lastName());
    generated.setSubscribed(request.subscribed());
    generated.setUserGroup(request.userGroup());
    generated.setUserId(request.userId());
    generated.setMailingLists(request.mailingLists());
    return generated;
  }

  public static com.telos.loops.internal.openapi.model.ContactDeleteRequest toGenerated(
      ContactDeleteRequest request) {
    var generated = new com.telos.loops.internal.openapi.model.ContactDeleteRequest();
    generated.setEmail(request.email());
    generated.setUserId(request.userId());
    return generated;
  }

  // ============================================================
  // Response Mappings: Generated OpenAPI Models → Public API
  // ============================================================

  public static ContactResponse fromGenerated(
      com.telos.loops.internal.openapi.model.ContactSuccessResponse response) {
    return new ContactResponse(response.getId(), null);
  }

  public static ContactResponse fromGenerated(
      com.telos.loops.internal.openapi.model.ContactDeleteResponse response) {
    return new ContactResponse(null, response.getMessage());
  }

  public static Contact fromGenerated(com.telos.loops.internal.openapi.model.Contact contact) {
    // Convert mailingLists from Object to Map<String, Boolean>
    Map<String, Boolean> mailingLists = null;
    if (contact.getMailingLists() instanceof Map) {
      mailingLists = new HashMap<>();
      @SuppressWarnings("unchecked")
      Map<String, Object> rawMap = (Map<String, Object>) contact.getMailingLists();
      for (Map.Entry<String, Object> entry : rawMap.entrySet()) {
        if (entry.getValue() instanceof Boolean) {
          mailingLists.put(entry.getKey(), (Boolean) entry.getValue());
        }
      }
    }

    // Convert optInStatus enum to string
    String optInStatus = null;
    if (contact.getOptInStatus() != null) {
      optInStatus = contact.getOptInStatus().getValue();
    }

    return new Contact(
        contact.getId(),
        contact.getEmail(),
        contact.getFirstName(),
        contact.getLastName(),
        contact.getSource(),
        contact.getSubscribed(),
        contact.getUserGroup(),
        contact.getUserId(),
        mailingLists,
        optInStatus);
  }

  // ============================================================
  // Query Parameter Conversion
  // ============================================================

  public static Map<String, String> toQueryParams(ContactFindRequest request) {
    Map<String, String> params = new HashMap<>();
    if (request.email() != null) {
      params.put("email", request.email());
    }
    if (request.userId() != null) {
      params.put("userId", request.userId());
    }
    return params;
  }
}
