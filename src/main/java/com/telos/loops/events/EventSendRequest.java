package com.telos.loops.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telos.loops.error.LoopsValidationException;
import jakarta.annotation.Nonnull;
import java.util.Map;

/**
 * Request to send an event to Loops.
 *
 * <p>Events are used to trigger automated email sequences or track user behavior. You must provide
 * an event name and either an email or userId to identify the contact. Event-specific data can be
 * included via eventProperties, and contact properties can be updated via additionalProperties.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * EventSendRequest request = new EventSendRequest(
 *     "user@example.com",           // email
 *     null,                          // userId
 *     "purchase_completed",          // eventName (required)
 *     Map.of("amount", 99.99,        // eventProperties
 *            "productId", "prod-123"),
 *     null,                          // mailingLists
 *     Map.of("lastPurchase", "2024-01-15")  // additionalProperties
 * );
 * }</pre>
 *
 * @param email the email address of the contact associated with this event
 * @param userId your application's unique identifier for the contact
 * @param eventName the name of the event (required, must match an event configured in Loops)
 * @param eventProperties event-specific properties (e.g., purchase amount, product ID)
 * @param mailingLists map of mailing list IDs to subscription status to update for this contact
 * @param additionalProperties contact properties to update when this event is sent
 * @throws LoopsValidationException if eventName is null or blank
 */
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
