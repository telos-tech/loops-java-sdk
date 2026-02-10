package com.telos.loops.ips;

import com.fasterxml.jackson.core.type.TypeReference;
import com.telos.loops.internal.CoreSender;
import com.telos.loops.model.RequestOptions;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class DedicatedIpsClient {
  private static final String LIST_PATH = "/dedicated-sending-ips";

  private final CoreSender sender;

  public DedicatedIpsClient(CoreSender sender) {
    this.sender = Objects.requireNonNull(sender);
  }

  // ============================================================
  // List Operations
  // ============================================================

  public List<String> list() {
    return list(RequestOptions.none());
  }

  public List<String> list(RequestOptions options) {
    return sender.getList(
        LIST_PATH, new HashMap<>(), new TypeReference<List<String>>() {}, options);
  }

  public CompletableFuture<List<String>> listAsync() {
    return listAsync(RequestOptions.none());
  }

  public CompletableFuture<List<String>> listAsync(RequestOptions options) {
    return sender.getListAsync(
        LIST_PATH, new HashMap<>(), new TypeReference<List<String>>() {}, options);
  }
}
