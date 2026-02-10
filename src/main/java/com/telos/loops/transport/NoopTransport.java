package com.telos.loops.transport;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoopTransport implements Transport {
  private static final Logger log = LoggerFactory.getLogger(NoopTransport.class);

  private final TransportResponse response;

  public NoopTransport() {
    this(new TransportResponse(200, Collections.emptyMap(), new byte[0]));
  }

  public NoopTransport(int statusCode, String jsonBody) {
    this.response =
        new TransportResponse(
            statusCode,
            Collections.emptyMap(),
            jsonBody == null ? new byte[0] : jsonBody.getBytes(StandardCharsets.UTF_8));
  }

  public NoopTransport(TransportResponse response) {
    this.response = response;
  }

  @Override
  public TransportResponse execute(TransportRequest request) {
    log.debug("NoopTransport.execute: req={}, res={}", request, response);
    return response;
  }

  @Override
  public CompletableFuture<TransportResponse> executeAsync(TransportRequest request) {
    log.debug("NoopTransport.executeAsync: req={}, res={}", request, response);
    return CompletableFuture.completedFuture(response);
  }
}
