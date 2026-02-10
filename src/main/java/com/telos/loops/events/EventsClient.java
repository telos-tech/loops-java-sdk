package com.telos.loops.events;

import com.telos.loops.error.LoopsValidationException;
import com.telos.loops.internal.CoreSender;
import com.telos.loops.internal.mappers.EventsMapper;
import com.telos.loops.model.RequestOptions;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

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

  public EventResponse send(EventSendRequest request) {
    return send(request, null, RequestOptions.none());
  }

  public EventResponse send(EventSendRequest request, String idempotencyKey) {
    return send(request, idempotencyKey, RequestOptions.none());
  }

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

  public CompletableFuture<EventResponse> sendAsync(EventSendRequest request) {
    return sendAsync(request, null, RequestOptions.none());
  }

  public CompletableFuture<EventResponse> sendAsync(
      EventSendRequest request, String idempotencyKey) {
    return sendAsync(request, idempotencyKey, RequestOptions.none());
  }

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
