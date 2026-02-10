/**
 * Core client for interacting with the Loops.so API.
 *
 * <p>This package contains the main entry point for the Loops Java SDK. The {@link
 * com.telos.loops.LoopsClient} provides access to all API operations through specialized client
 * interfaces for contacts, events, transactional emails, and more.
 *
 * <h2>Key Classes</h2>
 *
 * <ul>
 *   <li>{@link com.telos.loops.LoopsClient} - Main client providing access to all API operations
 * </ul>
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * // Create a client with your API key
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * // Test the API key
 * ApiKeyTestResponse response = client.apiKey().test();
 *
 * // Create a contact
 * ContactCreateRequest request = new ContactCreateRequest(
 *     "user@example.com",
 *     null,
 *     "John",
 *     "Doe",
 *     null,
 *     true,
 *     null
 * );
 * client.contacts().create(request);
 *
 * // Send an event
 * EventSendRequest event = new EventSendRequest(
 *     "eventName",
 *     "user@example.com",
 *     null,
 *     null,
 *     null
 * );
 * client.events().send(event);
 * }</pre>
 *
 * @see com.telos.loops.LoopsClient
 * @see com.telos.loops.contacts
 * @see com.telos.loops.events
 * @see com.telos.loops.transactional
 */
package com.telos.loops;
