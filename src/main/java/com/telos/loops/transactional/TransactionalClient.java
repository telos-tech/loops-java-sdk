package com.telos.loops.transactional;

import com.telos.loops.error.LoopsValidationException;
import com.telos.loops.internal.CoreSender;
import com.telos.loops.internal.mappers.TransactionalMapper;
import com.telos.loops.model.RequestOptions;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class TransactionalClient {
  private static final String TRANSACTIONAL_PATH = "/transactional";
  private static final int MAX_IDEMPOTENCY_KEY_LENGTH = 100;
  private static final int MIN_PER_PAGE = 10;
  private static final int MAX_PER_PAGE = 50;
  private final CoreSender sender;

  public TransactionalClient(CoreSender sender) {
    this.sender = Objects.requireNonNull(sender);
  }

  // ============================================================
  // Send Operations
  // ============================================================

  public TransactionalResponse send(TransactionalSendRequest request) {
    return send(request, null, RequestOptions.none());
  }

  public TransactionalResponse send(TransactionalSendRequest request, String idempotencyKey) {
    return send(request, idempotencyKey, RequestOptions.none());
  }

  public TransactionalResponse send(
      TransactionalSendRequest request, String idempotencyKey, RequestOptions options) {
    validateIdempotencyKey(idempotencyKey);

    var generatedRequest = TransactionalMapper.toGenerated(request);
    var optionsWithIdempotency = addIdempotencyKey(options, idempotencyKey);

    var generatedResponse =
        sender.postJson(
            TRANSACTIONAL_PATH,
            generatedRequest,
            com.telos.loops.internal.openapi.model.TransactionalSuccessResponse.class,
            optionsWithIdempotency);
    return TransactionalMapper.fromGenerated(generatedResponse);
  }

  public CompletableFuture<TransactionalResponse> sendAsync(TransactionalSendRequest request) {
    return sendAsync(request, null, RequestOptions.none());
  }

  public CompletableFuture<TransactionalResponse> sendAsync(
      TransactionalSendRequest request, String idempotencyKey) {
    return sendAsync(request, idempotencyKey, RequestOptions.none());
  }

  public CompletableFuture<TransactionalResponse> sendAsync(
      TransactionalSendRequest request, String idempotencyKey, RequestOptions options) {
    validateIdempotencyKey(idempotencyKey);

    var generatedRequest = TransactionalMapper.toGenerated(request);
    var optionsWithIdempotency = addIdempotencyKey(options, idempotencyKey);

    return sender
        .postJsonAsync(
            TRANSACTIONAL_PATH,
            generatedRequest,
            com.telos.loops.internal.openapi.model.TransactionalSuccessResponse.class,
            optionsWithIdempotency)
        .thenApply(TransactionalMapper::fromGenerated);
  }

  // ============================================================
  // List Operations
  // ============================================================

  public TransactionalListResponse list() {
    return list(null, null, RequestOptions.none());
  }

  public TransactionalListResponse list(Integer perPage, String cursor) {
    return list(perPage, cursor, RequestOptions.none());
  }

  public TransactionalListResponse list(Integer perPage, String cursor, RequestOptions options) {
    validatePerPage(perPage);

    Map<String, String> queryParams = new HashMap<>();
    if (perPage != null) {
      queryParams.put("perPage", String.valueOf(perPage));
    }
    if (cursor != null) {
      queryParams.put("cursor", cursor);
    }

    var generatedResponse =
        sender.get(
            TRANSACTIONAL_PATH,
            queryParams,
            com.telos.loops.internal.openapi.model.ListTransactionalsResponse.class,
            options);
    return TransactionalMapper.fromGenerated(generatedResponse);
  }

  public CompletableFuture<TransactionalListResponse> listAsync() {
    return listAsync(null, null, RequestOptions.none());
  }

  public CompletableFuture<TransactionalListResponse> listAsync(Integer perPage, String cursor) {
    return listAsync(perPage, cursor, RequestOptions.none());
  }

  public CompletableFuture<TransactionalListResponse> listAsync(
      Integer perPage, String cursor, RequestOptions options) {
    validatePerPage(perPage);

    Map<String, String> queryParams = new HashMap<>();
    if (perPage != null) {
      queryParams.put("perPage", String.valueOf(perPage));
    }
    if (cursor != null) {
      queryParams.put("cursor", cursor);
    }

    return sender
        .getAsync(
            TRANSACTIONAL_PATH,
            queryParams,
            com.telos.loops.internal.openapi.model.ListTransactionalsResponse.class,
            options)
        .thenApply(TransactionalMapper::fromGenerated);
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

  private void validatePerPage(Integer perPage) {
    if (perPage != null && (perPage < MIN_PER_PAGE || perPage > MAX_PER_PAGE)) {
      throw new LoopsValidationException(
          "perPage must be between " + MIN_PER_PAGE + " and " + MAX_PER_PAGE + ", got: " + perPage);
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
