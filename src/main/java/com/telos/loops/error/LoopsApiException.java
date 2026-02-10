package com.telos.loops.error;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Thrown when the Loops API returns an error response.
 *
 * <p>This exception is raised for all HTTP error responses (4xx, 5xx status codes) returned by the
 * Loops API. It captures the HTTP status code, raw response body, and parsed error message for
 * detailed error handling.
 *
 * <h2>Common Scenarios</h2>
 *
 * <ul>
 *   <li><b>400 Bad Request:</b> Invalid request parameters or malformed JSON
 *   <li><b>401 Unauthorized:</b> Invalid or missing API key
 *   <li><b>404 Not Found:</b> Resource not found
 *   <li><b>429 Too Many Requests:</b> Rate limit exceeded (see {@link RateLimitExceededException})
 *   <li><b>500 Internal Server Error:</b> Server-side error
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
 *     client.contacts().create(request);
 * } catch (LoopsApiException e) {
 *     System.err.println("API Error: " + e.getMessage());
 *     System.err.println("Status Code: " + e.statusCode());
 *     System.err.println("Error: " + e.error());
 *     System.err.println("Raw Body: " + e.rawBody());
 * } catch (LoopsValidationException e) {
 *     System.err.println("Validation Error: " + e.getMessage());
 * }
 * }</pre>
 *
 * <h2>Retry Behavior</h2>
 *
 * <p>This exception does not trigger automatic retries. For rate limit errors (HTTP 429), a {@link
 * RateLimitExceededException} is thrown instead, which includes retry-after information.
 *
 * @see RateLimitExceededException
 * @see LoopsValidationException
 */
public class LoopsApiException extends RuntimeException {
  private final int statusCode;
  private final String rawBody;
  private final String error;

  /**
   * Constructs a new LoopsApiException with a custom message.
   *
   * <p>This constructor is used for generic error conditions where no HTTP response is available.
   *
   * @param message the error message
   */
  public LoopsApiException(String message) {
    super(message);
    this.statusCode = 0;
    this.rawBody = null;
    this.error = null;
  }

  /**
   * Constructs a new LoopsApiException from an HTTP error response.
   *
   * <p>The error message is automatically extracted from the response body's "message" or "error"
   * field if present, otherwise defaults to "HTTP {statusCode}".
   *
   * @param statusCode the HTTP status code from the error response
   * @param rawBody the raw response body as a string
   */
  public LoopsApiException(int statusCode, String rawBody) {
    super(extractMessage(statusCode, rawBody));
    this.statusCode = statusCode;
    this.rawBody = rawBody;
    this.error = extractError(rawBody);
  }

  /**
   * Returns the HTTP status code from the error response.
   *
   * <p>Returns 0 if the exception was constructed without an HTTP response.
   *
   * @return the HTTP status code, or 0 if not applicable
   */
  public int statusCode() {
    return statusCode;
  }

  /**
   * Returns the raw response body from the API.
   *
   * <p>This can be useful for debugging or logging the full error response.
   *
   * @return the raw response body, or null if not available
   */
  public String rawBody() {
    return rawBody;
  }

  /**
   * Returns the parsed error message from the response body.
   *
   * <p>This is extracted from the "error" field in the JSON response if present.
   *
   * @return the error message, or null if not available
   */
  public String error() {
    return error;
  }

  private static String extractMessage(int statusCode, String rawBody) {
    String message = "HTTP " + statusCode;
    try {
      if (rawBody != null && !rawBody.isEmpty()) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(rawBody);
        if (node.has("message")) {
          message += ": " + node.get("message").asText();
        } else if (node.has("error")) {
          message += ": " + node.get("error").asText();
        }
      }
    } catch (Exception e) {
      // Ignore parsing errors
    }
    return message;
  }

  private static String extractError(String rawBody) {
    try {
      if (rawBody != null && !rawBody.isEmpty()) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(rawBody);
        if (node.has("error")) {
          return node.get("error").asText();
        }
      }
    } catch (Exception e) {
      // Ignore parsing errors
    }
    return null;
  }
}
