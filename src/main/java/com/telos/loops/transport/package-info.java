/**
 * Transport layer abstraction for HTTP communication in the Loops Java SDK.
 *
 * <p>This package provides the HTTP transport abstraction layer used by the SDK to communicate
 * with the Loops API. It allows for custom HTTP client implementations while providing a default
 * OkHttp-based transport.
 *
 * <h2>Key Classes</h2>
 *
 * <ul>
 *   <li>{@link com.telos.loops.transport.Transport} - Interface for HTTP transport implementations
 *   <li>{@link com.telos.loops.transport.OkHttpTransport} - Default OkHttp-based transport
 *   <li>{@link com.telos.loops.transport.TransportRequest} - Represents an HTTP request
 *   <li>{@link com.telos.loops.transport.TransportResponse} - Represents an HTTP response
 *   <li>{@link com.telos.loops.transport.HttpMethod} - HTTP methods (GET, POST, PUT, DELETE)
 * </ul>
 *
 * <h2>Default Transport</h2>
 *
 * <p>By default, the SDK uses {@link com.telos.loops.transport.OkHttpTransport} which is built on
 * top of OkHttp. This provides reliable HTTP communication with connection pooling and modern HTTP
 * features.
 *
 * <pre>{@code
 * // Default usage - OkHttpTransport is used automatically
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 * }</pre>
 *
 * <h2>Custom Transport</h2>
 *
 * <p>For advanced use cases, you can implement the {@link com.telos.loops.transport.Transport}
 * interface to use a different HTTP client or add custom behavior (logging, proxying, etc.).
 *
 * <pre>{@code
 * // Example: Custom transport with logging
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
 *         logger.info("Executing request: {} {}", request.httpMethod(), request.url());
 *         TransportResponse response = delegate.execute(request);
 *         logger.info("Response status: {}", response.statusCode());
 *         return response;
 *     }
 *
 *     @Override
 *     public CompletableFuture<TransportResponse> executeAsync(TransportRequest request) {
 *         logger.info("Executing async request: {} {}", request.httpMethod(), request.url());
 *         return delegate.executeAsync(request)
 *             .thenApply(response -> {
 *                 logger.info("Async response status: {}", response.statusCode());
 *                 return response;
 *             });
 *     }
 * }
 *
 * // Use custom transport
 * Transport baseTransport = new OkHttpTransport();
 * Transport loggingTransport = new LoggingTransport(baseTransport, myLogger);
 * // Note: Custom transport configuration would need to be added to LoopsClient.Builder
 * }</pre>
 *
 * <p><b>Note:</b> Currently, the SDK does not expose a way to inject custom transports via the
 * public API. This is an internal abstraction that may be exposed in future versions.
 *
 * @see com.telos.loops.transport.Transport
 * @see com.telos.loops.transport.OkHttpTransport
 */
package com.telos.loops.transport;
