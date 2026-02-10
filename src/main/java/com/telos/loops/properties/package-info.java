/**
 * Client and models for managing custom contact properties in the Loops.so API.
 *
 * <p>This package provides functionality to view custom contact properties configured in your
 * Loops account. Custom properties allow you to store additional data about your contacts beyond
 * the standard fields (email, name, etc.).
 *
 * <h2>Key Classes</h2>
 *
 * <ul>
 *   <li>{@link com.telos.loops.properties.ContactPropertiesClient} - Client for contact property
 *       operations
 *   <li>{@link com.telos.loops.properties.ContactProperty} - Represents a custom contact property
 * </ul>
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * ContactPropertiesClient properties = client.contactProperties();
 *
 * // Get all custom contact properties
 * List<ContactProperty> allProperties = properties.get();
 * for (ContactProperty property : allProperties) {
 *     System.out.println("Property: " + property.key());
 *     System.out.println("  Label: " + property.label());
 *     System.out.println("  Type: " + property.type());
 * }
 *
 * // Async operations
 * CompletableFuture<List<ContactProperty>> future = properties.getAsync();
 * }</pre>
 *
 * <p><b>Note:</b> To set custom property values on contacts, use the {@link
 * com.telos.loops.contacts.ContactsClient} with the customProperties parameter when creating or
 * updating contacts.
 *
 * @see com.telos.loops.LoopsClient
 * @see com.telos.loops.properties.ContactPropertiesClient
 * @see com.telos.loops.contacts.ContactsClient
 */
package com.telos.loops.properties;
