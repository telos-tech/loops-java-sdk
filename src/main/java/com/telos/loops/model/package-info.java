/**
 * Common models and shared types used across the Loops Java SDK.
 *
 * <p>This package contains data models and value objects that are shared across multiple API
 * clients. These models represent common concepts like request options and shared response types.
 *
 * <h2>Key Classes</h2>
 *
 * <ul>
 *   <li>{@link com.telos.loops.model.RequestOptions} - Optional parameters for API requests (e.g.,
 *       custom headers, timeouts)
 * </ul>
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * // Using default request options (most common)
 * ContactResponse response = client.contacts().create(request);
 *
 * // Using RequestOptions.none() explicitly (equivalent to above)
 * ContactResponse response2 = client.contacts().create(request, RequestOptions.none());
 *
 * // Using custom request options (advanced usage)
 * RequestOptions options = RequestOptions.builder()
 *         .addHeader("X-Custom-Header", "value")
 *         .timeout(Duration.ofSeconds(30))
 *         .build();
 * ContactResponse response3 = client.contacts().create(request, options);
 * }</pre>
 *
 * <h2>Request Options</h2>
 *
 * <p>All API client methods accept an optional {@link com.telos.loops.model.RequestOptions}
 * parameter. This allows you to customize individual requests with:
 *
 * <ul>
 *   <li>Custom HTTP headers
 *   <li>Request timeouts
 *   <li>Other request-specific configuration
 * </ul>
 *
 * <p>Most users will use {@code RequestOptions.none()} (the default) and don't need to worry about
 * this. Custom options are provided for advanced use cases.
 *
 * @see com.telos.loops.model.RequestOptions
 */
package com.telos.loops.model;
