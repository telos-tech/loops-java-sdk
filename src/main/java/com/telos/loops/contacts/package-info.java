/**
 * Client and models for managing contacts in the Loops.so API.
 *
 * <p>This package provides full CRUD operations for contacts in your Loops audience. Contacts can
 * be identified by email address or user ID, and can have custom properties, mailing list
 * subscriptions, and subscription status.
 *
 * <h2>Key Classes</h2>
 *
 * <ul>
 *   <li>{@link com.telos.loops.contacts.ContactsClient} - Client for contact operations
 *   <li>{@link com.telos.loops.contacts.Contact} - Represents a contact in Loops
 *   <li>{@link com.telos.loops.contacts.ContactCreateRequest} - Request to create a new contact
 *   <li>{@link com.telos.loops.contacts.ContactUpdateRequest} - Request to update a contact
 *   <li>{@link com.telos.loops.contacts.ContactFindRequest} - Request to find contacts
 *   <li>{@link com.telos.loops.contacts.ContactDeleteRequest} - Request to delete a contact
 *   <li>{@link com.telos.loops.contacts.ContactResponse} - Response from contact operations
 * </ul>
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * ContactsClient contacts = client.contacts();
 *
 * // Create a contact
 * ContactCreateRequest createReq = new ContactCreateRequest(
 *     "user@example.com",
 *     "user123",  // userId
 *     "John",     // firstName
 *     "Doe",      // lastName
 *     "website",  // source
 *     true,       // subscribed
 *     Map.of("plan", "premium")  // customProperties
 * );
 * ContactResponse response = contacts.create(createReq);
 *
 * // Update a contact
 * ContactUpdateRequest updateReq = new ContactUpdateRequest(
 *     "user@example.com",
 *     null,
 *     null,
 *     null,
 *     "Newsletter,Updates",  // mailingLists
 *     true,
 *     Map.of("lastLogin", "2026-02-10")
 * );
 * contacts.update(updateReq);
 *
 * // Find a contact
 * ContactFindRequest findReq = new ContactFindRequest("user@example.com", null);
 * List<Contact> found = contacts.find(findReq);
 *
 * // Delete a contact
 * ContactDeleteRequest deleteReq = new ContactDeleteRequest("user@example.com", null);
 * contacts.delete(deleteReq);
 *
 * // Async operations
 * CompletableFuture<ContactResponse> future = contacts.createAsync(createReq);
 * }</pre>
 *
 * @see com.telos.loops.LoopsClient
 * @see com.telos.loops.contacts.ContactsClient
 */
package com.telos.loops.contacts;
