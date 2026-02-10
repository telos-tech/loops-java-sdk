package com.telos.loops.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telos.loops.error.LoopsValidationException;
import jakarta.annotation.Nonnull;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = false)
public record EventSendRequest(
    String email,
    String userId,
    @Nonnull String eventName,
    Map<String, Object> eventProperties,
    Map<String, Boolean> mailingLists,
    Map<String, Object> additionalProperties) {

  public EventSendRequest {
    if (eventName == null || eventName.isBlank()) {
      throw new LoopsValidationException("eventName is required and cannot be blank");
    }
  }
}
