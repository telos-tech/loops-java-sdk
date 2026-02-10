package com.telos.loops.apikey;

import com.telos.loops.internal.CoreSender;
import com.telos.loops.model.RequestOptions;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ApiKeyClient {
  private static final String TEST_PATH = "/api-key";

  private final CoreSender sender;

  public ApiKeyClient(CoreSender sender) {
    this.sender = Objects.requireNonNull(sender);
  }

  // ============================================================
  // Test Operations
  // ============================================================

  public ApiKeyTestResponse test() {
    return test(RequestOptions.none());
  }

  public ApiKeyTestResponse test(RequestOptions options) {
    return sender.get(TEST_PATH, new HashMap<>(), ApiKeyTestResponse.class, options);
  }

  public CompletableFuture<ApiKeyTestResponse> testAsync() {
    return testAsync(RequestOptions.none());
  }

  public CompletableFuture<ApiKeyTestResponse> testAsync(RequestOptions options) {
    return sender.getAsync(TEST_PATH, new HashMap<>(), ApiKeyTestResponse.class, options);
  }
}
