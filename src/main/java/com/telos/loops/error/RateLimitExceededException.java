package com.telos.loops.error;

public class RateLimitExceededException extends LoopsApiException {
  private final long retryAfterSeconds;

  public RateLimitExceededException(int statusCode, String rawBody, long retryAfterSeconds) {
    super(statusCode, rawBody);
    this.retryAfterSeconds = retryAfterSeconds;
  }

  public long retryAfterSeconds() {
    return retryAfterSeconds;
  }
}
