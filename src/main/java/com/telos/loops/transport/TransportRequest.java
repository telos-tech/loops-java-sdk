package com.telos.loops.transport;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

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
}
