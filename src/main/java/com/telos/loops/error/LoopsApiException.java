package com.telos.loops.error;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoopsApiException extends RuntimeException {
  private final int statusCode;
  private final String rawBody;
  private final String error;

  public LoopsApiException(String message) {
    super(message);
    this.statusCode = 0;
    this.rawBody = null;
    this.error = null;
  }

  public LoopsApiException(int statusCode, String rawBody) {
    super(extractMessage(statusCode, rawBody));
    this.statusCode = statusCode;
    this.rawBody = rawBody;
    this.error = extractError(rawBody);
  }

  public int statusCode() {
    return statusCode;
  }

  public String rawBody() {
    return rawBody;
  }

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
