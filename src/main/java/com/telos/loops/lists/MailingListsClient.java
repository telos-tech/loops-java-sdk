package com.telos.loops.lists;

import com.fasterxml.jackson.core.type.TypeReference;
import com.telos.loops.internal.CoreSender;
import com.telos.loops.internal.mappers.MailingListsMapper;
import com.telos.loops.model.RequestOptions;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Client for managing mailing lists in Loops.
 *
 * <p>This client provides read-only access to view all mailing lists configured in your Loops
 * account. Mailing lists can be used to organize and segment your contacts.
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * // List all mailing lists
 * List<MailingList> lists = client.mailingLists().list();
 *
 * // Display list information
 * for (MailingList list : lists) {
 *     System.out.println("List ID: " + list.id());
 *     System.out.println("List Name: " + list.name());
 *     System.out.println("Is Default: " + list.isDefault());
 * }
 *
 * // Async example
 * CompletableFuture<List<MailingList>> future =
 *     client.mailingLists().listAsync();
 * }</pre>
 *
 * <p>All methods are available in both synchronous and asynchronous (CompletableFuture) variants.
 *
 * @see com.telos.loops.LoopsClient
 */
public class MailingListsClient {
  private static final String LIST_PATH = "/lists";

  private final CoreSender sender;

  public MailingListsClient(CoreSender sender) {
    this.sender = Objects.requireNonNull(sender);
  }

  // ============================================================
  // List Operations
  // ============================================================

  /**
   * Lists all mailing lists in your Loops account.
   *
   * <p>This method uses default request options.
   *
   * @return a list of all mailing lists
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @see #listAsync() for the asynchronous variant
   */
  public List<MailingList> list() {
    return list(RequestOptions.none());
  }

  /**
   * Lists all mailing lists in your Loops account with custom request options.
   *
   * @param options additional request options such as custom headers
   * @return a list of all mailing lists
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @see #listAsync(RequestOptions) for the asynchronous variant
   */
  public List<MailingList> list(RequestOptions options) {
    var generatedLists =
        sender.getList(
            LIST_PATH,
            new HashMap<>(),
            new TypeReference<List<com.telos.loops.internal.openapi.model.MailingList>>() {},
            options);
    return generatedLists.stream()
        .map(MailingListsMapper::fromGenerated)
        .collect(Collectors.toList());
  }

  /**
   * Asynchronously lists all mailing lists in your Loops account.
   *
   * <p>This method uses default request options.
   *
   * @return a CompletableFuture containing a list of all mailing lists
   * @see #list() for the synchronous variant
   */
  public CompletableFuture<List<MailingList>> listAsync() {
    return listAsync(RequestOptions.none());
  }

  /**
   * Asynchronously lists all mailing lists in your Loops account with custom request options.
   *
   * @param options additional request options such as custom headers
   * @return a CompletableFuture containing a list of all mailing lists
   * @see #list(RequestOptions) for the synchronous variant
   */
  public CompletableFuture<List<MailingList>> listAsync(RequestOptions options) {
    return sender
        .getListAsync(
            LIST_PATH,
            new HashMap<>(),
            new TypeReference<List<com.telos.loops.internal.openapi.model.MailingList>>() {},
            options)
        .thenApply(
            generatedLists ->
                generatedLists.stream()
                    .map(MailingListsMapper::fromGenerated)
                    .collect(Collectors.toList()));
  }
}
