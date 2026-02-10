package com.telos.loops.events;

import com.telos.loops.error.LoopsValidationException;
import com.telos.loops.internal.CoreSender;
import com.telos.loops.internal.mappers.EventsMapper;
import com.telos.loops.model.RequestOptions;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Client for sending events in Loops.
 *
 * <p>This client allows you to send events that trigger automated email campaigns. Events can
 * include custom properties and support idempotency keys to prevent duplicate processing.
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * // Send an event
 * Map<String, Object> eventProperties = new HashMap<>();
 * eventProperties.put("productName", "Premium Plan");
 * eventProperties.put("price", 99.99);
 *
 * EventSendRequest request = new EventSendRequest(
 *     "purchase_completed",  // eventName
 *     "user@example.com",  // email
 *     null,  // userId (optional)
 *     null,  // mailingLists
 *     eventProperties  // custom event properties
 * );
 *
 * EventResponse response = client.events().send(request);
 *
 * // Send with idempotency key to prevent duplicates
 * String idempotencyKey = "order-123-event";
 * EventResponse response2 = client.events().send(request, idempotencyKey);
 *
 * // Async example
 * CompletableFuture<EventResponse> future =
 *     client.events().sendAsync(request, idempotencyKey);
 * }</pre>
 *
 * <p>All methods are available in both synchronous and asynchronous (CompletableFuture) variants.
 *
 * @see com.telos.loops.LoopsClient
 */
public class EventsClient {
  private static final String SEND_PATH = "/events/send";
  private static final int MAX_IDEMPOTENCY_KEY_LENGTH = 100;

  private final CoreSender sender;

  public EventsClient(CoreSender sender) {
    this.sender = Objects.requireNonNull(sender);
  }

  // ============================================================
  // Send Operations
  // ============================================================

  /**
   * Sends an event to Loops.
   *
   * <p>This method uses default request options and no idempotency key.
   *
   * @param request the request object containing event details
   * @return the response containing the event submission status
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if the request fails validation
   * @see #sendAsync(EventSendRequest) for the asynchronous variant
   */
  public EventResponse send(EventSendRequest request) {
    return send(request, null, RequestOptions.none());
  }

  /**
   * Sends an event to Loops with an idempotency key.
   *
   * <p>The idempotency key ensures that duplicate events are not processed if the same key is used
   * within a certain time window.
   *
   * @param request the request object containing event details
   * @param idempotencyKey optional idempotency key (max 100 characters) to prevent duplicate
   *     processing
   * @return the response containing the event submission status
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if the request fails validation or
   *     idempotency key exceeds 100 characters
   * @see #sendAsync(EventSendRequest, String) for the asynchronous variant
   */
  public EventResponse send(EventSendRequest request, String idempotencyKey) {
    return send(request, idempotencyKey, RequestOptions.none());
  }

  /**
   * Sends an event to Loops with an idempotency key and custom request options.
   *
   * @param request the request object containing event details
   * @param idempotencyKey optional idempotency key (max 100 characters) to prevent duplicate
   *     processing
   * @param options additional request options such as custom headers
   * @return the response containing the event submission status
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if the request fails validation or
   *     idempotency key exceeds 100 characters
   * @see #sendAsync(EventSendRequest, String, RequestOptions) for the asynchronous variant
   */
  public EventResponse send(
      EventSendRequest request, String idempotencyKey, RequestOptions options) {
    validateIdempotencyKey(idempotencyKey);

    var generatedRequest = EventsMapper.toGenerated(request);
    var optionsWithIdempotency = addIdempotencyKey(options, idempotencyKey);

    var generatedResponse =
        sender.postJson(
            SEND_PATH,
            generatedRequest,
            com.telos.loops.internal.openapi.model.EventSuccessResponse.class,
            optionsWithIdempotency);
    return EventsMapper.fromGenerated(generatedResponse);
  }

  /**
   * Asynchronously sends an event to Loops.
   *
   * <p>This method uses default request options and no idempotency key.
   *
   * @param request the request object containing event details
   * @return a CompletableFuture containing the event submission status
   * @see #send(EventSendRequest) for the synchronous variant
   */
  public CompletableFuture<EventResponse> sendAsync(EventSendRequest request) {
    return sendAsync(request, null, RequestOptions.none());
  }

  /**
   * Asynchronously sends an event to Loops with an idempotency key.
   *
   * <p>The idempotency key ensures that duplicate events are not processed if the same key is used
   * within a certain time window.
   *
   * @param request the request object containing event details
   * @param idempotencyKey optional idempotency key (max 100 characters) to prevent duplicate
   *     processing
   * @return a CompletableFuture containing the event submission status
   * @see #send(EventSendRequest, String) for the synchronous variant
   */
  public CompletableFuture<EventResponse> sendAsync(
      EventSendRequest request, String idempotencyKey) {
    return sendAsync(request, idempotencyKey, RequestOptions.none());
  }

  /**
   * Asynchronously sends an event to Loops with an idempotency key and custom request options.
   *
   * @param request the request object containing event details
   * @param idempotencyKey optional idempotency key (max 100 characters) to prevent duplicate
   *     processing
   * @param options additional request options such as custom headers
   * @return a CompletableFuture containing the event submission status
   * @see #send(EventSendRequest, String, RequestOptions) for the synchronous variant
   */
  public CompletableFuture<EventResponse> sendAsync(
      EventSendRequest request, String idempotencyKey, RequestOptions options) {
    validateIdempotencyKey(idempotencyKey);

    var generatedRequest = EventsMapper.toGenerated(request);
    var optionsWithIdempotency = addIdempotencyKey(options, idempotencyKey);

    return sender
        .postJsonAsync(
            SEND_PATH,
            generatedRequest,
            com.telos.loops.internal.openapi.model.EventSuccessResponse.class,
            optionsWithIdempotency)
        .thenApply(EventsMapper::fromGenerated);
  }

  // ============================================================
  // Helper Methods
  // ============================================================

  private void validateIdempotencyKey(String idempotencyKey) {
    if (idempotencyKey != null && idempotencyKey.length() > MAX_IDEMPOTENCY_KEY_LENGTH) {
      throw new LoopsValidationException(
          "Idempotency key must not exceed "
              + MAX_IDEMPOTENCY_KEY_LENGTH
              + " characters, got: "
              + idempotencyKey.length());
    }
  }

  private RequestOptions addIdempotencyKey(RequestOptions options, String idempotencyKey) {
    if (idempotencyKey == null) {
      return options;
    }

    Map<String, String> headers = new HashMap<>(options.headers());
    headers.put("Idempotency-Key", idempotencyKey);
    return new RequestOptions(headers);
  }
}
