package com.telos.loops.transport;

import java.util.concurrent.CompletableFuture;

public interface Transport {
  TransportResponse execute(TransportRequest request);

  CompletableFuture<TransportResponse> executeAsync(TransportRequest request);
}
