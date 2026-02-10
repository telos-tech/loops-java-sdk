/**
 * Client and models for sending and managing transactional emails via the Loops.so API.
 *
 * <p>This package provides functionality to send one-off transactional emails (like password
 * resets, order confirmations, etc.) and retrieve information about sent emails.
 *
 * <h2>Key Classes</h2>
 *
 * <ul>
 *   <li>{@link com.telos.loops.transactional.TransactionalClient} - Client for transactional email
 *       operations
 *   <li>{@link com.telos.loops.transactional.TransactionalSendRequest} - Request to send a
 *       transactional email
 *   <li>{@link com.telos.loops.transactional.TransactionalResponse} - Response from sending an
 *       email
 *   <li>{@link com.telos.loops.transactional.TransactionalEmail} - Information about a sent email
 * </ul>
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * TransactionalClient transactional = client.transactional();
 *
 * // Send a transactional email
 * TransactionalSendRequest sendRequest = new TransactionalSendRequest(
 *     "transactionalId",        // Your transactional email template ID
 *     "user@example.com",       // email
 *     Map.of(                   // dataVariables (optional)
 *         "name", "John",
 *         "resetUrl", "https://example.com/reset/abc123"
 *     ),
 *     null,                     // addToAudience (optional)
 *     null,                     // attachments (optional)
 *     null                      // userId (optional)
 * );
 * TransactionalResponse response = transactional.send(sendRequest);
 *
 * // Async operations
 * CompletableFuture<TransactionalResponse> future =
 *     transactional.sendAsync(sendRequest);
 * }</pre>
 *
 * @see com.telos.loops.LoopsClient
 * @see com.telos.loops.transactional.TransactionalClient
 */
package com.telos.loops.transactional;
