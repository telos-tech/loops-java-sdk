package com.telos.loops.transport;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

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
}
