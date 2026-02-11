package com.telos.loops.transport;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

/**
 * Represents an HTTP request to be executed by a transport layer.
 *
 * <p>
 * This is an internal abstraction used by the SDK to decouple the public API
 * from the
 * underlying HTTP client implementation. Users typically don't need to
 * construct these directly.
 *
 * @param httpMethod the HTTP method (GET, POST, PUT, DELETE, PATCH)
 * @param url        the complete URL to send the request to
 * @param headers    HTTP headers to include in the request (immutable copy is
 *                   created)
 * @param body       the request body as bytes (immutable copy is created)
 */
public record TransportRequest(
    HttpMethod httpMethod, String url, Map<String, String> headers, byte[] body) {

  public TransportRequest {
    headers = headers == null ? Map.of() : Map.copyOf(headers);
    body = body == null ? new byte[0] : Arrays.copyOf(body, body.length);
  }

  @Override
  public String toString() {
    return "TransportRequest{"
        + "httpMethod="
        + httpMethod
        + ", url='"
        + url
        + '\''
        + ", headers="
        + headers
        + ", body="
        + new String(body, StandardCharsets.UTF_8)
        + '}';
  }

  /**
   * Creates a new builder for {@link TransportRequest}.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link TransportRequest}. */
  public static final class Builder {
    private HttpMethod httpMethod;
    private String url;
    private java.util.Map<String, String> headers;
    private byte[] body;

    private Builder() {
    }

    public Builder httpMethod(HttpMethod httpMethod) {
      this.httpMethod = httpMethod;
      return this;
    }

    public Builder url(String url) {
      this.url = url;
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

    public Builder body(byte[] body) {
      this.body = body;
      return this;
    }

    public TransportRequest build() {
      return new TransportRequest(httpMethod, url, headers, body);
    }
  }
}
