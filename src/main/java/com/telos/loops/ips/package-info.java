/**
 * Client and models for viewing dedicated IP addresses in the Loops.so API.
 *
 * <p>This package provides functionality to retrieve information about dedicated sending IP
 * addresses configured in your Loops account. Dedicated IPs can improve email deliverability and
 * sender reputation for high-volume senders.
 *
 * <h2>Key Classes</h2>
 *
 * <ul>
 *   <li>{@link com.telos.loops.ips.DedicatedIpsClient} - Client for dedicated IP operations
 * </ul>
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * DedicatedIpsClient ips = client.dedicatedIps();
 *
 * // Get all dedicated IP addresses
 * var allIps = ips.get();
 * System.out.println("Dedicated IPs: " + allIps);
 *
 * // Async operations
 * var future = ips.getAsync();
 * }</pre>
 *
 * <p><b>Note:</b> Dedicated IPs are typically used by enterprise customers with high email volume.
 * Contact Loops support to set up dedicated IPs for your account.
 *
 * @see com.telos.loops.LoopsClient
 * @see com.telos.loops.ips.DedicatedIpsClient
 */
package com.telos.loops.ips;
