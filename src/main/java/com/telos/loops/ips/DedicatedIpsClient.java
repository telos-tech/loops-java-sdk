package com.telos.loops.ips;

import com.fasterxml.jackson.core.type.TypeReference;
import com.telos.loops.internal.CoreSender;
import com.telos.loops.model.RequestOptions;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Client for viewing dedicated sending IP addresses in Loops.
 *
 * <p>This client provides read-only access to view the dedicated IP addresses configured for your
 * Loops account. Dedicated IPs are used for sending emails and help with deliverability and sender
 * reputation management.
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * // List all dedicated IP addresses
 * List<String> ips = client.dedicatedIps().list();
 *
 * // Display IP addresses
 * for (String ip : ips) {
 *     System.out.println("Dedicated IP: " + ip);
 * }
 *
 * // Async example
 * CompletableFuture<List<String>> future =
 *     client.dedicatedIps().listAsync();
 * }</pre>
 *
 * <p>All methods are available in both synchronous and asynchronous (CompletableFuture) variants.
 *
 * @see com.telos.loops.LoopsClient
 */
public class DedicatedIpsClient {
  private static final String LIST_PATH = "/dedicated-sending-ips";

  private final CoreSender sender;

  public DedicatedIpsClient(CoreSender sender) {
    this.sender = Objects.requireNonNull(sender);
  }

  // ============================================================
  // List Operations
  // ============================================================

  /**
   * Lists all dedicated IP addresses for your Loops account.
   *
   * <p>This method uses default request options.
   *
   * @return a list of IP addresses as strings
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @see #listAsync() for the asynchronous variant
   */
  public List<String> list() {
    return list(RequestOptions.none());
  }

  /**
   * Lists all dedicated IP addresses for your Loops account with custom request options.
   *
   * @param options additional request options such as custom headers
   * @return a list of IP addresses as strings
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @see #listAsync(RequestOptions) for the asynchronous variant
   */
  public List<String> list(RequestOptions options) {
    return sender.getList(
        LIST_PATH, new HashMap<>(), new TypeReference<List<String>>() {}, options);
  }

  /**
   * Asynchronously lists all dedicated IP addresses for your Loops account.
   *
   * <p>This method uses default request options.
   *
   * @return a CompletableFuture containing a list of IP addresses as strings
   * @see #list() for the synchronous variant
   */
  public CompletableFuture<List<String>> listAsync() {
    return listAsync(RequestOptions.none());
  }

  /**
   * Asynchronously lists all dedicated IP addresses for your Loops account with custom request
   * options.
   *
   * @param options additional request options such as custom headers
   * @return a CompletableFuture containing a list of IP addresses as strings
   * @see #list(RequestOptions) for the synchronous variant
   */
  public CompletableFuture<List<String>> listAsync(RequestOptions options) {
    return sender.getListAsync(
        LIST_PATH, new HashMap<>(), new TypeReference<List<String>>() {}, options);
  }
}
