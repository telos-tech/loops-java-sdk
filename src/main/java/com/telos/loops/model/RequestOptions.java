package com.telos.loops.model;

import java.util.Collections;
import java.util.Map;

/**
 * Optional request configuration for API calls.
 *
 * <p>Allows you to customize individual API requests by providing additional HTTP headers. Use
 * {@link #none()} for default behavior with no custom headers.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * // No custom options
 * RequestOptions options = RequestOptions.none();
 *
 * // With custom headers
 * RequestOptions options = new RequestOptions(
 *     Map.of("X-Custom-Header", "value")
 * );
 * }</pre>
 *
 * @param headers additional HTTP headers to include in the request (immutable copy is created)
 */
public record RequestOptions(Map<String, String> headers) {

  public RequestOptions {
    headers = headers == null ? Map.of() : Map.copyOf(headers);
  }

  /**
   * Creates a RequestOptions instance with no custom headers.
   *
   * @return a RequestOptions with empty headers map
   */
  public static RequestOptions none() {
    return new RequestOptions(Collections.emptyMap());
  }
}
