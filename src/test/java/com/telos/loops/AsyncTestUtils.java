package com.telos.loops;

import static org.awaitility.Awaitility.await;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/** Utilities for testing asynchronous operations with CompletableFuture. */
public class AsyncTestUtils {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);

  /**
   * Waits for a CompletableFuture to complete and returns its result.
   *
   * @param future the future to wait for
   * @param <T> the type of the result
   * @return the result of the future
   * @throws RuntimeException if the future fails or times out
   */
  public static <T> T awaitCompletion(CompletableFuture<T> future) {
    return awaitCompletion(future, DEFAULT_TIMEOUT);
  }

  /**
   * Waits for a CompletableFuture to complete with a custom timeout.
   *
   * @param future the future to wait for
   * @param timeout the maximum time to wait
   * @param <T> the type of the result
   * @return the result of the future
   * @throws RuntimeException if the future fails or times out
   */
  public static <T> T awaitCompletion(CompletableFuture<T> future, Duration timeout) {
    try {
      return future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
    } catch (Exception e) {
      throw new RuntimeException("Future failed or timed out", e);
    }
  }

  /**
   * Waits for a CompletableFuture to complete exceptionally.
   *
   * @param future the future to wait for
   * @param expectedExceptionType the expected exception type
   * @param <T> the type of the result
   * @param <E> the type of the exception
   * @return the exception that was thrown
   */
  public static <T, E extends Throwable> E awaitException(
      CompletableFuture<T> future, Class<E> expectedExceptionType) {
    try {
      future.get(DEFAULT_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
      throw new AssertionError("Expected future to fail with " + expectedExceptionType.getName());
    } catch (Exception e) {
      Throwable cause = e.getCause();
      if (expectedExceptionType.isInstance(cause)) {
        return expectedExceptionType.cast(cause);
      }
      throw new AssertionError(
          "Expected exception of type "
              + expectedExceptionType.getName()
              + " but got "
              + (cause != null ? cause.getClass().getName() : e.getClass().getName()),
          e);
    }
  }

  /**
   * Waits until a condition becomes true using Awaitility.
   *
   * @param condition the condition to wait for
   */
  public static void waitUntil(Runnable condition) {
    await().atMost(DEFAULT_TIMEOUT).untilAsserted(condition::run);
  }

  /**
   * Waits for a specific duration (useful for testing retry logic).
   *
   * @param duration the duration to wait
   */
  public static void sleep(Duration duration) {
    try {
      Thread.sleep(duration.toMillis());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Sleep interrupted", e);
    }
  }

  private AsyncTestUtils() {
    // Utility class
  }
}
