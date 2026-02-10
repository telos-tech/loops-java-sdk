package com.telos.loops.error;

/**
 * Thrown when client-side validation fails before making an API request.
 *
 * <p>This exception is raised when request parameters fail validation checks performed by the SDK
 * before sending the request to the Loops API. This helps catch common errors early and avoid
 * unnecessary API calls.
 *
 * <h2>Common Scenarios</h2>
 *
 * <ul>
 *   <li><b>Missing required fields:</b> Email or userId not provided when required
 *   <li><b>Invalid formats:</b> Malformed email addresses or invalid data types
 *   <li><b>Constraint violations:</b> Values outside acceptable ranges or patterns
 *   <li><b>Mutually exclusive fields:</b> Both email and userId provided when only one is allowed
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
 *     // This will throw LoopsValidationException if request validation fails
 *     ContactCreateRequest request = new ContactCreateRequest(
 *         null,  // Missing email - validation error!
 *         null,
 *         "John",
 *         "Doe",
 *         null,
 *         true,
 *         null
 *     );
 *     client.contacts().create(request);
 * } catch (LoopsValidationException e) {
 *     System.err.println("Validation failed: " + e.getMessage());
 *     // Fix the request and retry
 * } catch (LoopsApiException e) {
 *     System.err.println("API error: " + e.getMessage());
 * }
 * }</pre>
 *
 * <h2>Retry Behavior</h2>
 *
 * <p>Validation exceptions should not be retried without fixing the request parameters. These
 * errors indicate a problem with the request itself, not a transient server issue.
 *
 * @see LoopsApiException
 * @see RateLimitExceededException
 */
public class LoopsValidationException extends RuntimeException {
  /**
   * Constructs a new LoopsValidationException with the specified error message.
   *
   * @param message a description of the validation failure
   */
  public LoopsValidationException(String message) {
    super(message);
  }
}
