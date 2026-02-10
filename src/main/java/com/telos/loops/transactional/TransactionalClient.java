package com.telos.loops.transactional;

import com.telos.loops.error.LoopsValidationException;
import com.telos.loops.internal.CoreSender;
import com.telos.loops.internal.mappers.TransactionalMapper;
import com.telos.loops.model.RequestOptions;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Client for sending and managing transactional emails in Loops.
 *
 * <p>This client provides operations for sending one-off transactional emails (e.g., password
 * resets, order confirmations) and listing sent transactional emails. Transactional emails support
 * idempotency keys to prevent duplicate sends.
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * // Send a transactional email
 * Map<String, Object> dataVariables = new HashMap<>();
 * dataVariables.put("resetLink", "https://example.com/reset/token123");
 * dataVariables.put("userName", "John");
 *
 * TransactionalSendRequest request = new TransactionalSendRequest(
 *     "user@example.com",  // email
 *     "clt1234abcd",  // transactionalId (from Loops dashboard)
 *     null,  // addToAudience (optional)
 *     dataVariables  // email template variables
 * );
 *
 * TransactionalResponse response = client.transactional().send(request);
 *
 * // Send with idempotency key to prevent duplicate sends
 * String idempotencyKey = "password-reset-user123";
 * client.transactional().send(request, idempotencyKey);
 *
 * // List sent transactional emails (with pagination)
 * TransactionalListResponse listResponse = client.transactional().list(25, null);
 * System.out.println("Sent emails: " + listResponse.transactionalEmails().size());
 *
 * // Get next page using cursor
 * String nextCursor = listResponse.cursor();
 * if (nextCursor != null) {
 *     TransactionalListResponse nextPage = client.transactional().list(25, nextCursor);
 * }
 *
 * // Async example
 * CompletableFuture<TransactionalResponse> future =
 *     client.transactional().sendAsync(request, idempotencyKey);
 * }</pre>
 *
 * <p>All methods are available in both synchronous and asynchronous (CompletableFuture) variants.
 *
 * @see com.telos.loops.LoopsClient
 */
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

  /**
   * Sends a transactional email.
   *
   * <p>This method uses default request options and no idempotency key.
   *
   * @param request the request object containing email details and template variables
   * @return the response containing the email submission status
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if the request fails validation
   * @see #sendAsync(TransactionalSendRequest) for the asynchronous variant
   */
  public TransactionalResponse send(TransactionalSendRequest request) {
    return send(request, null, RequestOptions.none());
  }

  /**
   * Sends a transactional email with an idempotency key.
   *
   * <p>The idempotency key ensures that duplicate emails are not sent if the same key is used
   * within a certain time window.
   *
   * @param request the request object containing email details and template variables
   * @param idempotencyKey optional idempotency key (max 100 characters) to prevent duplicate sends
   * @return the response containing the email submission status
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if the request fails validation or
   *     idempotency key exceeds 100 characters
   * @see #sendAsync(TransactionalSendRequest, String) for the asynchronous variant
   */
  public TransactionalResponse send(TransactionalSendRequest request, String idempotencyKey) {
    return send(request, idempotencyKey, RequestOptions.none());
  }

  /**
   * Sends a transactional email with an idempotency key and custom request options.
   *
   * @param request the request object containing email details and template variables
   * @param idempotencyKey optional idempotency key (max 100 characters) to prevent duplicate sends
   * @param options additional request options such as custom headers
   * @return the response containing the email submission status
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if the request fails validation or
   *     idempotency key exceeds 100 characters
   * @see #sendAsync(TransactionalSendRequest, String, RequestOptions) for the asynchronous variant
   */
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

  /**
   * Asynchronously sends a transactional email.
   *
   * <p>This method uses default request options and no idempotency key.
   *
   * @param request the request object containing email details and template variables
   * @return a CompletableFuture containing the email submission status
   * @see #send(TransactionalSendRequest) for the synchronous variant
   */
  public CompletableFuture<TransactionalResponse> sendAsync(TransactionalSendRequest request) {
    return sendAsync(request, null, RequestOptions.none());
  }

  /**
   * Asynchronously sends a transactional email with an idempotency key.
   *
   * <p>The idempotency key ensures that duplicate emails are not sent if the same key is used
   * within a certain time window.
   *
   * @param request the request object containing email details and template variables
   * @param idempotencyKey optional idempotency key (max 100 characters) to prevent duplicate sends
   * @return a CompletableFuture containing the email submission status
   * @see #send(TransactionalSendRequest, String) for the synchronous variant
   */
  public CompletableFuture<TransactionalResponse> sendAsync(
      TransactionalSendRequest request, String idempotencyKey) {
    return sendAsync(request, idempotencyKey, RequestOptions.none());
  }

  /**
   * Asynchronously sends a transactional email with an idempotency key and custom request options.
   *
   * @param request the request object containing email details and template variables
   * @param idempotencyKey optional idempotency key (max 100 characters) to prevent duplicate sends
   * @param options additional request options such as custom headers
   * @return a CompletableFuture containing the email submission status
   * @see #send(TransactionalSendRequest, String, RequestOptions) for the synchronous variant
   */
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

  /**
   * Lists all transactional emails.
   *
   * <p>This method uses default pagination settings and request options.
   *
   * @return the response containing a list of transactional emails and pagination information
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @see #listAsync() for the asynchronous variant
   */
  public TransactionalListResponse list() {
    return list(null, null, RequestOptions.none());
  }

  /**
   * Lists transactional emails with pagination parameters.
   *
   * @param perPage number of results per page (must be between 10 and 50)
   * @param cursor pagination cursor from a previous response, or null for the first page
   * @return the response containing a list of transactional emails and pagination information
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if perPage is not between 10 and 50
   * @see #listAsync(Integer, String) for the asynchronous variant
   */
  public TransactionalListResponse list(Integer perPage, String cursor) {
    return list(perPage, cursor, RequestOptions.none());
  }

  /**
   * Lists transactional emails with pagination parameters and custom request options.
   *
   * @param perPage number of results per page (must be between 10 and 50)
   * @param cursor pagination cursor from a previous response, or null for the first page
   * @param options additional request options such as custom headers
   * @return the response containing a list of transactional emails and pagination information
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if perPage is not between 10 and 50
   * @see #listAsync(Integer, String, RequestOptions) for the asynchronous variant
   */
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

  /**
   * Asynchronously lists all transactional emails.
   *
   * <p>This method uses default pagination settings and request options.
   *
   * @return a CompletableFuture containing a list of transactional emails and pagination
   *     information
   * @see #list() for the synchronous variant
   */
  public CompletableFuture<TransactionalListResponse> listAsync() {
    return listAsync(null, null, RequestOptions.none());
  }

  /**
   * Asynchronously lists transactional emails with pagination parameters.
   *
   * @param perPage number of results per page (must be between 10 and 50)
   * @param cursor pagination cursor from a previous response, or null for the first page
   * @return a CompletableFuture containing a list of transactional emails and pagination
   *     information
   * @see #list(Integer, String) for the synchronous variant
   */
  public CompletableFuture<TransactionalListResponse> listAsync(Integer perPage, String cursor) {
    return listAsync(perPage, cursor, RequestOptions.none());
  }

  /**
   * Asynchronously lists transactional emails with pagination parameters and custom request
   * options.
   *
   * @param perPage number of results per page (must be between 10 and 50)
   * @param cursor pagination cursor from a previous response, or null for the first page
   * @param options additional request options such as custom headers
   * @return a CompletableFuture containing a list of transactional emails and pagination
   *     information
   * @see #list(Integer, String, RequestOptions) for the synchronous variant
   */
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
