package com.telos.loops.transport;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

/**
 * Represents an HTTP response from the transport layer.
 *
 * <p>
 * This is an internal abstraction used by the SDK to decouple the public API
 * from the
 * underlying HTTP client implementation. Users typically don't need to
 * construct these directly.
 *
 * @param status   the HTTP status code (e.g., 200, 404, 500)
 * @param headers  HTTP response headers (immutable copy is created)
 * @param response the response body as bytes (immutable copy is created)
 */
public record TransportResponse(int status, Map<String, String> headers, byte[] response) {

  public TransportResponse {
    headers = headers == null ? Map.of() : Map.copyOf(headers);
    response = response == null ? new byte[0] : Arrays.copyOf(response, response.length);
  }

  @Override
  public String toString() {
    return "TransportResponse{"
        + "status="
        + status
        + ", headers="
        + headers
        + ", response="
        + new String(response, StandardCharsets.UTF_8)
        + '}';
  }

  /**
   * Creates a new builder for {@link TransportResponse}.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link TransportResponse}. */
  public static final class Builder {
    private int status;
    private java.util.Map<String, String> headers;
    private byte[] response;

    private Builder() {
    }

    public Builder status(int status) {
      this.status = status;
      return this;
    }

    public Builder headers(Map<String, String> headers) {
      this.headers = headers;
      return this;
    }

    public Builder addHeader(String key, String value) {
      if (this.headers == null) {
        this.headers = new java.util.HashMap<>();
      }
      this.headers.put(key, value);
      return this;
    }

    public Builder response(byte[] response) {
      this.response = response;
      return this;
    }

    public TransportResponse build() {
      return new TransportResponse(status, headers, response);
    }
  }
}
