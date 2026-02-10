/**
 * Client and models for API key validation in the Loops.so API.
 *
 * <p>This package provides functionality to test and validate your Loops API key. This is useful
 * for verifying authentication during application startup or troubleshooting connection issues.
 *
 * <h2>Key Classes</h2>
 *
 * <ul>
 *   <li>{@link com.telos.loops.apikey.ApiKeyClient} - Client for API key operations
 *   <li>{@link com.telos.loops.apikey.ApiKeyTestResponse} - Response from testing an API key
 * </ul>
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * ApiKeyClient apiKey = client.apiKey();
 *
 * // Test the API key
 * try {
 *     ApiKeyTestResponse response = apiKey.test();
 *     if (response.success()) {
 *         System.out.println("API key is valid!");
 *         System.out.println("Team: " + response.teamName());
 *     }
 * } catch (LoopsApiException e) {
 *     System.err.println("API key is invalid: " + e.getMessage());
 * }
 *
 * // Async operations
 * CompletableFuture<ApiKeyTestResponse> future = apiKey.testAsync();
 * future.thenAccept(response -> {
 *     if (response.success()) {
 *         System.out.println("API key validated successfully");
 *     }
 * });
 * }</pre>
 *
 * <p><b>Note:</b> Testing the API key makes an actual API call to Loops. Use this sparingly to
 * avoid unnecessary API usage.
 *
 * @see com.telos.loops.LoopsClient
 * @see com.telos.loops.apikey.ApiKeyClient
 * @see com.telos.loops.error.LoopsApiException
 */
package com.telos.loops.apikey;
