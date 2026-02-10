package com.telos.loops.error;

/**
 * Thrown when the Loops API rate limit has been exceeded (HTTP 429).
 *
 * <p>This exception is raised when you've made too many requests to the Loops API within a given
 * time window. The API returns an HTTP 429 status code along with a "Retry-After" header
 * indicating when you can retry the request.
 *
 * <h2>Rate Limit Information</h2>
 *
 * <p>The Loops API enforces rate limits to ensure fair usage and system stability. When you exceed
 * these limits, this exception provides the number of seconds to wait before retrying.
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * try {
 *     client.contacts().create(request);
 * } catch (RateLimitExceededException e) {
 *     long waitSeconds = e.retryAfterSeconds();
 *     System.err.println("Rate limit exceeded. Retry after " + waitSeconds + " seconds.");
 *     System.err.println("Status Code: " + e.statusCode());
 *
 *     // Option 1: Wait and retry
 *     Thread.sleep(waitSeconds * 1000);
 *     client.contacts().create(request);  // Retry
 *
 *     // Option 2: Schedule retry for later
 *     scheduler.schedule(() -> {
 *         client.contacts().create(request);
 *     }, waitSeconds, TimeUnit.SECONDS);
 * } catch (LoopsApiException e) {
 *     System.err.println("API error: " + e.getMessage());
 * }
 * }</pre>
 *
 * <h2>Retry Behavior</h2>
 *
 * <p>The SDK does not automatically retry rate-limited requests. It's the caller's responsibility
 * to implement retry logic with appropriate backoff. Always respect the {@link #retryAfterSeconds}
 * value to avoid further rate limiting.
 *
 * <p><b>Best Practices:</b>
 *
 * <ul>
 *   <li>Implement exponential backoff for retries
 *   <li>Respect the retry-after duration from {@link #retryAfterSeconds()}
 *   <li>Consider implementing a request queue with rate limiting
 *   <li>Monitor your API usage to avoid hitting limits
 * </ul>
 *
 * @see LoopsApiException
 * @see LoopsValidationException
 */
public class RateLimitExceededException extends LoopsApiException {
  private final long retryAfterSeconds;

  /**
   * Constructs a new RateLimitExceededException from an HTTP 429 response.
   *
   * @param statusCode the HTTP status code (typically 429)
   * @param rawBody the raw response body from the API
   * @param retryAfterSeconds the number of seconds to wait before retrying, from the Retry-After
   *     header
   */
  public RateLimitExceededException(int statusCode, String rawBody, long retryAfterSeconds) {
    super(statusCode, rawBody);
    this.retryAfterSeconds = retryAfterSeconds;
  }

  /**
   * Returns the number of seconds to wait before retrying the request.
   *
   * <p>This value comes from the "Retry-After" header in the API response. Callers should wait at
   * least this long before making another request to avoid further rate limiting.
   *
   * @return the retry-after duration in seconds
   */
  public long retryAfterSeconds() {
    return retryAfterSeconds;
  }
}
