package com.telos.loops.transport;

import java.util.concurrent.CompletableFuture;

/**
 * Interface for HTTP transport implementations in the Loops SDK.
 *
 * <p>This interface abstracts the HTTP client layer, allowing different HTTP client
 * implementations to be used with the SDK. The default implementation uses OkHttp via {@link
 * OkHttpTransport}.
 *
 * <h2>Default Implementation</h2>
 *
 * <p>The SDK uses {@link OkHttpTransport} by default, which provides:
 *
 * <ul>
 *   <li>HTTP/2 support
 *   <li>Connection pooling
 *   <li>Automatic retry for failed connections
 *   <li>Efficient resource usage
 * </ul>
 *
 * <h2>Custom Implementations</h2>
 *
 * <p>You can implement this interface to use a different HTTP client or add custom behavior like
 * logging, metrics collection, or request/response modification.
 *
 * <h3>Example: Custom Transport with Logging</h3>
 *
 * <pre>{@code
 * public class LoggingTransport implements Transport {
 *     private final Transport delegate;
 *     private final Logger logger;
 *
 *     public LoggingTransport(Transport delegate, Logger logger) {
 *         this.delegate = delegate;
 *         this.logger = logger;
 *     }
 *
 *     @Override
 *     public TransportResponse execute(TransportRequest request) {
 *         logger.info("Request: {} {}", request.httpMethod(), request.url());
 *         long start = System.currentTimeMillis();
 *
 *         try {
 *             TransportResponse response = delegate.execute(request);
 *             long duration = System.currentTimeMillis() - start;
 *             logger.info("Response: {} ({}ms)", response.statusCode(), duration);
 *             return response;
 *         } catch (Exception e) {
 *             logger.error("Request failed", e);
 *             throw e;
 *         }
 *     }
 *
 *     @Override
 *     public CompletableFuture<TransportResponse> executeAsync(TransportRequest request) {
 *         logger.info("Async request: {} {}", request.httpMethod(), request.url());
 *         long start = System.currentTimeMillis();
 *
 *         return delegate.executeAsync(request)
 *             .whenComplete((response, error) -> {
 *                 long duration = System.currentTimeMillis() - start;
 *                 if (error != null) {
 *                     logger.error("Async request failed ({}ms)", duration, error);
 *                 } else {
 *                     logger.info("Async response: {} ({}ms)",
 *                         response.statusCode(), duration);
 *                 }
 *             });
 *     }
 * }
 * }</pre>
 *
 * <h3>Example: Custom Transport with Proxy</h3>
 *
 * <pre>{@code
 * public class ProxyTransport implements Transport {
 *     private final OkHttpClient client;
 *
 *     public ProxyTransport(String proxyHost, int proxyPort) {
 *         Proxy proxy = new Proxy(Proxy.Type.HTTP,
 *             new InetSocketAddress(proxyHost, proxyPort));
 *         this.client = new OkHttpClient.Builder()
 *             .proxy(proxy)
 *             .build();
 *     }
 *
 *     @Override
 *     public TransportResponse execute(TransportRequest request) {
 *         OkHttpTransport transport = new OkHttpTransport(client);
 *         return transport.execute(request);
 *     }
 *
 *     @Override
 *     public CompletableFuture<TransportResponse> executeAsync(TransportRequest request) {
 *         OkHttpTransport transport = new OkHttpTransport(client);
 *         return transport.executeAsync(request);
 *     }
 * }
 * }</pre>
 *
 * <h2>Implementation Requirements</h2>
 *
 * <p>When implementing this interface, ensure that:
 *
 * <ul>
 *   <li>Both {@link #execute} and {@link #executeAsync} methods handle all HTTP methods (GET,
 *       POST, PUT, DELETE)
 *   <li>Request headers from {@link TransportRequest#headers()} are properly set
 *   <li>Request body from {@link TransportRequest#body()} is sent when present
 *   <li>Response headers are captured in {@link TransportResponse#headers()}
 *   <li>Response body bytes are captured in {@link TransportResponse#response()}
 *   <li>HTTP status code is returned in {@link TransportResponse#status()}
 *   <li>Network failures throw appropriate exceptions
 *   <li>Thread safety is maintained for concurrent requests
 * </ul>
 *
 * <p><b>Note:</b> Currently, the SDK does not expose a way to inject custom transports via the
 * public API. This is planned for a future release.
 *
 * @see OkHttpTransport
 * @see TransportRequest
 * @see TransportResponse
 */
public interface Transport {
  /**
   * Executes an HTTP request synchronously.
   *
   * <p>This method blocks until the HTTP request completes and a response is received.
   *
   * @param request the HTTP request to execute
   * @return the HTTP response
   * @throws RuntimeException if the request fails due to network error, timeout, or other issues
   */
  TransportResponse execute(TransportRequest request);

  /**
   * Executes an HTTP request asynchronously.
   *
   * <p>This method returns immediately with a {@link CompletableFuture} that will be completed
   * when the HTTP request finishes.
   *
   * @param request the HTTP request to execute
   * @return a CompletableFuture that will complete with the HTTP response
   */
  CompletableFuture<TransportResponse> executeAsync(TransportRequest request);
}
