/**
 * Exception classes for error handling in the Loops Java SDK.
 *
 * <p>This package contains all custom exceptions thrown by the Loops SDK. Understanding these
 * exceptions is essential for robust error handling in your application.
 *
 * <h2>Exception Hierarchy</h2>
 *
 * <pre>
 * RuntimeException
 *   |
 *   +-- LoopsApiException (HTTP errors from API)
 *   |     |
 *   |     +-- RateLimitExceededException (HTTP 429)
 *   |
 *   +-- LoopsValidationException (Client-side validation errors)
 * </pre>
 *
 * <h2>Key Classes</h2>
 *
 * <ul>
 *   <li>{@link com.telos.loops.error.LoopsApiException} - Thrown for API error responses (4xx,
 *       5xx)
 *   <li>{@link com.telos.loops.error.RateLimitExceededException} - Thrown when rate limit is
 *       exceeded (429)
 *   <li>{@link com.telos.loops.error.LoopsValidationException} - Thrown for client-side validation
 *       failures
 * </ul>
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * try {
 *     // Make API call
 *     client.contacts().create(request);
 *
 * } catch (RateLimitExceededException e) {
 *     // Handle rate limiting with retry
 *     long retryAfter = e.retryAfterSeconds();
 *     System.err.println("Rate limited. Retry after " + retryAfter + " seconds");
 *     Thread.sleep(retryAfter * 1000);
 *     // Retry the request
 *
 * } catch (LoopsValidationException e) {
 *     // Handle validation errors (fix request and retry)
 *     System.err.println("Validation failed: " + e.getMessage());
 *     // Fix the request parameters
 *
 * } catch (LoopsApiException e) {
 *     // Handle other API errors
 *     System.err.println("API error (HTTP " + e.statusCode() + "): " + e.getMessage());
 *     System.err.println("Error: " + e.error());
 *     System.err.println("Raw response: " + e.rawBody());
 * }
 * }</pre>
 *
 * <h2>Error Handling Best Practices</h2>
 *
 * <ul>
 *   <li>Catch {@link com.telos.loops.error.RateLimitExceededException} first to handle rate
 *       limiting
 *   <li>Catch {@link com.telos.loops.error.LoopsValidationException} to handle client-side
 *       validation
 *   <li>Catch {@link com.telos.loops.error.LoopsApiException} for all other API errors
 *   <li>Implement retry logic with exponential backoff for transient failures
 *   <li>Log error details for debugging ({@code statusCode()}, {@code error()}, {@code rawBody()})
 * </ul>
 *
 * @see com.telos.loops.error.LoopsApiException
 * @see com.telos.loops.error.RateLimitExceededException
 * @see com.telos.loops.error.LoopsValidationException
 */
package com.telos.loops.error;
