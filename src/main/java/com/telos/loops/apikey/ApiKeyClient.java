package com.telos.loops.apikey;

import com.telos.loops.internal.CoreSender;
import com.telos.loops.model.RequestOptions;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Client for testing API key validity in Loops.
 *
 * <p>This client provides a simple test endpoint to verify that your API key is valid and properly
 * authenticated. This is useful for health checks and initialization verification.
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * // Test the API key
 * ApiKeyTestResponse response = client.apiKey().test();
 *
 * if (response.success()) {
 *     System.out.println("API key is valid!");
 *     System.out.println("Team: " + response.teamName());
 * } else {
 *     System.out.println("API key is invalid");
 * }
 *
 * // Async example
 * CompletableFuture<ApiKeyTestResponse> future =
 *     client.apiKey().testAsync();
 * }</pre>
 *
 * <p>All methods are available in both synchronous and asynchronous (CompletableFuture) variants.
 *
 * @see com.telos.loops.LoopsClient
 */
public class ApiKeyClient {
  private static final String TEST_PATH = "/api-key";

  private final CoreSender sender;

  public ApiKeyClient(CoreSender sender) {
    this.sender = Objects.requireNonNull(sender);
  }

  // ============================================================
  // Test Operations
  // ============================================================

  /**
   * Tests the validity of the API key.
   *
   * <p>This method uses default request options.
   *
   * @return the response indicating whether the API key is valid and team information
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @see #testAsync() for the asynchronous variant
   */
  public ApiKeyTestResponse test() {
    return test(RequestOptions.none());
  }

  /**
   * Tests the validity of the API key with custom request options.
   *
   * @param options additional request options such as custom headers
   * @return the response indicating whether the API key is valid and team information
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @see #testAsync(RequestOptions) for the asynchronous variant
   */
  public ApiKeyTestResponse test(RequestOptions options) {
    return sender.get(TEST_PATH, new HashMap<>(), ApiKeyTestResponse.class, options);
  }

  /**
   * Asynchronously tests the validity of the API key.
   *
   * <p>This method uses default request options.
   *
   * @return a CompletableFuture containing the response indicating whether the API key is valid
   *     and team information
   * @see #test() for the synchronous variant
   */
  public CompletableFuture<ApiKeyTestResponse> testAsync() {
    return testAsync(RequestOptions.none());
  }

  /**
   * Asynchronously tests the validity of the API key with custom request options.
   *
   * @param options additional request options such as custom headers
   * @return a CompletableFuture containing the response indicating whether the API key is valid
   *     and team information
   * @see #test(RequestOptions) for the synchronous variant
   */
  public CompletableFuture<ApiKeyTestResponse> testAsync(RequestOptions options) {
    return sender.getAsync(TEST_PATH, new HashMap<>(), ApiKeyTestResponse.class, options);
  }
}
