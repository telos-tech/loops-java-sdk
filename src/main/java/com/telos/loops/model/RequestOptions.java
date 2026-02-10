package com.telos.loops.model;

import java.util.Collections;
import java.util.Map;

public record RequestOptions(Map<String, String> headers) {

  public RequestOptions {
    headers = headers == null ? Map.of() : Map.copyOf(headers);
  }

  public static RequestOptions none() {
    return new RequestOptions(Collections.emptyMap());
  }
}
