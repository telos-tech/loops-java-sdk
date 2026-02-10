/**
 * Client and models for viewing mailing lists in the Loops.so API.
 *
 * <p>This package provides functionality to retrieve information about mailing lists configured in
 * your Loops account. Mailing lists are used to segment your audience for targeted email
 * campaigns.
 *
 * <h2>Key Classes</h2>
 *
 * <ul>
 *   <li>{@link com.telos.loops.lists.MailingListsClient} - Client for mailing list operations
 *   <li>{@link com.telos.loops.lists.MailingList} - Represents a mailing list
 * </ul>
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * MailingListsClient lists = client.mailingLists();
 *
 * // Get all mailing lists
 * List<MailingList> allLists = lists.get();
 * for (MailingList list : allLists) {
 *     System.out.println("List: " + list.name() + " (ID: " + list.id() + ")");
 *     System.out.println("  Subscribed: " + list.isSubscribed());
 * }
 *
 * // Async operations
 * CompletableFuture<List<MailingList>> future = lists.getAsync();
 * }</pre>
 *
 * <p><b>Note:</b> To subscribe contacts to mailing lists, use the {@link
 * com.telos.loops.contacts.ContactsClient} with the mailingLists parameter when creating or
 * updating contacts.
 *
 * @see com.telos.loops.LoopsClient
 * @see com.telos.loops.lists.MailingListsClient
 * @see com.telos.loops.contacts.ContactsClient
 */
package com.telos.loops.lists;
