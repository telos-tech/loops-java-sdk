package com.telos.loops.internal.mappers;

import com.telos.loops.events.EventResponse;
import com.telos.loops.events.EventSendRequest;

public class EventsMapper {

  // ============================================================
  // Request Mappings: Public API → Generated OpenAPI Models
  // ============================================================

  public static com.telos.loops.internal.openapi.model.EventRequest toGenerated(
      EventSendRequest request) {
    var generated = new com.telos.loops.internal.openapi.model.EventRequest();
    generated.setEmail(request.email());
    generated.setUserId(request.userId());
    generated.setEventName(request.eventName());
    generated.setEventProperties(request.eventProperties());
    generated.setMailingLists(request.mailingLists());
    return generated;
  }

  // ============================================================
  // Response Mappings: Generated OpenAPI Models → Public API
  // ============================================================

  public static EventResponse fromGenerated(
      com.telos.loops.internal.openapi.model.EventSuccessResponse response) {
    return new EventResponse(response.getSuccess());
  }
}
