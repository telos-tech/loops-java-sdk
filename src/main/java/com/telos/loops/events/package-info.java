/**
 * Client and models for sending events to the Loops.so API.
 *
 * <p>This package provides functionality to send events that trigger email campaigns in Loops.
 * Events can include custom properties and can target contacts by email address or user ID.
 *
 * <h2>Key Classes</h2>
 *
 * <ul>
 *   <li>{@link com.telos.loops.events.EventsClient} - Client for event operations
 *   <li>{@link com.telos.loops.events.EventSendRequest} - Request to send an event
 *   <li>{@link com.telos.loops.events.EventResponse} - Response from sending an event
 * </ul>
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * EventsClient events = client.events();
 *
 * // Send an event with email
 * EventSendRequest request = new EventSendRequest(
 *     "userSignedUp",           // eventName
 *     "user@example.com",       // email
 *     null,                     // userId (optional, use email or userId)
 *     null,                     // mailingLists (optional)
 *     Map.of(                   // eventProperties (optional)
 *         "plan", "premium",
 *         "source", "website"
 *     )
 * );
 * EventSendResponse response = events.send(request);
 *
 * // Send an event with userId
 * EventSendRequest userIdRequest = new EventSendRequest(
 *     "purchaseCompleted",
 *     null,                     // email
 *     "user123",                // userId
 *     null,
 *     Map.of(
 *         "amount", 99.99,
 *         "currency", "USD"
 *     )
 * );
 * events.send(userIdRequest);
 *
 * // Async operations
 * CompletableFuture<EventSendResponse> future = events.sendAsync(request);
 * }</pre>
 *
 * @see com.telos.loops.LoopsClient
 * @see com.telos.loops.events.EventsClient
 */
package com.telos.loops.events;
