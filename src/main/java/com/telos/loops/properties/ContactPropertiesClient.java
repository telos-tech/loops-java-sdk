package com.telos.loops.properties;

import com.fasterxml.jackson.core.type.TypeReference;
import com.telos.loops.internal.CoreSender;
import com.telos.loops.internal.mappers.ContactPropertiesMapper;
import com.telos.loops.model.RequestOptions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Client for managing custom contact properties in Loops.
 *
 * <p>This client allows you to create and list custom properties that can be attached to contacts.
 * Custom properties enable you to store additional data fields beyond the standard contact fields.
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * // Create a custom property
 * ContactPropertyCreateRequest request = new ContactPropertyCreateRequest(
 *     "accountTier",  // property name
 *     "string"  // data type (string, number, boolean, date)
 * );
 * ContactPropertyResponse response = client.contactProperties().create(request);
 *
 * // List all custom properties
 * List<ContactProperty> properties = client.contactProperties().list();
 *
 * // List properties filtered by type
 * List<ContactProperty> stringProps = client.contactProperties().list("string");
 *
 * // Display properties
 * for (ContactProperty prop : properties) {
 *     System.out.println("Property: " + prop.key() + " (" + prop.type() + ")");
 * }
 *
 * // Async example
 * CompletableFuture<ContactPropertyResponse> future =
 *     client.contactProperties().createAsync(request);
 * }</pre>
 *
 * <p>All methods are available in both synchronous and asynchronous (CompletableFuture) variants.
 *
 * @see com.telos.loops.LoopsClient
 */
public class ContactPropertiesClient {
  private static final String PROPERTIES_PATH = "/contacts/properties";

  private final CoreSender sender;

  public ContactPropertiesClient(CoreSender sender) {
    this.sender = Objects.requireNonNull(sender);
  }

  // ============================================================
  // Create Operations
  // ============================================================

  /**
   * Creates a new custom contact property.
   *
   * <p>This method uses default request options.
   *
   * @param request the request object containing property name and data type
   * @return the response containing the created property details
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if the request fails validation
   * @see #createAsync(ContactPropertyCreateRequest) for the asynchronous variant
   */
  public ContactPropertyResponse create(ContactPropertyCreateRequest request) {
    return create(request, RequestOptions.none());
  }

  /**
   * Creates a new custom contact property with custom request options.
   *
   * @param request the request object containing property name and data type
   * @param options additional request options such as custom headers
   * @return the response containing the created property details
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if the request fails validation
   * @see #createAsync(ContactPropertyCreateRequest, RequestOptions) for the asynchronous variant
   */
  public ContactPropertyResponse create(
      ContactPropertyCreateRequest request, RequestOptions options) {
    var generatedRequest = ContactPropertiesMapper.toGenerated(request);
    var generatedResponse =
        sender.postJson(
            PROPERTIES_PATH,
            generatedRequest,
            com.telos.loops.internal.openapi.model.ContactPropertySuccessResponse.class,
            options);
    return ContactPropertiesMapper.fromGenerated(generatedResponse);
  }

  /**
   * Asynchronously creates a new custom contact property.
   *
   * <p>This method uses default request options.
   *
   * @param request the request object containing property name and data type
   * @return a CompletableFuture containing the created property details
   * @see #create(ContactPropertyCreateRequest) for the synchronous variant
   */
  public CompletableFuture<ContactPropertyResponse> createAsync(
      ContactPropertyCreateRequest request) {
    return createAsync(request, RequestOptions.none());
  }

  /**
   * Asynchronously creates a new custom contact property with custom request options.
   *
   * @param request the request object containing property name and data type
   * @param options additional request options such as custom headers
   * @return a CompletableFuture containing the created property details
   * @see #create(ContactPropertyCreateRequest, RequestOptions) for the synchronous variant
   */
  public CompletableFuture<ContactPropertyResponse> createAsync(
      ContactPropertyCreateRequest request, RequestOptions options) {
    var generatedRequest = ContactPropertiesMapper.toGenerated(request);
    return sender
        .postJsonAsync(
            PROPERTIES_PATH,
            generatedRequest,
            com.telos.loops.internal.openapi.model.ContactPropertySuccessResponse.class,
            options)
        .thenApply(ContactPropertiesMapper::fromGenerated);
  }

  // ============================================================
  // List Operations
  // ============================================================

  /**
   * Lists all custom contact properties.
   *
   * <p>This method uses default request options and returns all properties regardless of type.
   *
   * @return a list of all custom contact properties
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @see #listAsync() for the asynchronous variant
   */
  public List<ContactProperty> list() {
    return list(null, RequestOptions.none());
  }

  /**
   * Lists custom contact properties filtered by type.
   *
   * @param listType optional filter for property type (e.g., "string", "number", "boolean",
   *     "date")
   * @return a list of custom contact properties matching the filter
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @see #listAsync(String) for the asynchronous variant
   */
  public List<ContactProperty> list(String listType) {
    return list(listType, RequestOptions.none());
  }

  /**
   * Lists custom contact properties filtered by type with custom request options.
   *
   * @param listType optional filter for property type (e.g., "string", "number", "boolean",
   *     "date")
   * @param options additional request options such as custom headers
   * @return a list of custom contact properties matching the filter
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @see #listAsync(String, RequestOptions) for the asynchronous variant
   */
  public List<ContactProperty> list(String listType, RequestOptions options) {
    Map<String, String> queryParams = new HashMap<>();
    if (listType != null) {
      queryParams.put("list", listType);
    }

    var generatedProperties =
        sender.getList(
            PROPERTIES_PATH,
            queryParams,
            new TypeReference<List<com.telos.loops.internal.openapi.model.ContactProperty>>() {},
            options);
    return generatedProperties.stream()
        .map(ContactPropertiesMapper::fromGenerated)
        .collect(Collectors.toList());
  }

  /**
   * Asynchronously lists all custom contact properties.
   *
   * <p>This method uses default request options and returns all properties regardless of type.
   *
   * @return a CompletableFuture containing a list of all custom contact properties
   * @see #list() for the synchronous variant
   */
  public CompletableFuture<List<ContactProperty>> listAsync() {
    return listAsync(null, RequestOptions.none());
  }

  /**
   * Asynchronously lists custom contact properties filtered by type.
   *
   * @param listType optional filter for property type (e.g., "string", "number", "boolean",
   *     "date")
   * @return a CompletableFuture containing a list of custom contact properties matching the filter
   * @see #list(String) for the synchronous variant
   */
  public CompletableFuture<List<ContactProperty>> listAsync(String listType) {
    return listAsync(listType, RequestOptions.none());
  }

  /**
   * Asynchronously lists custom contact properties filtered by type with custom request options.
   *
   * @param listType optional filter for property type (e.g., "string", "number", "boolean",
   *     "date")
   * @param options additional request options such as custom headers
   * @return a CompletableFuture containing a list of custom contact properties matching the filter
   * @see #list(String, RequestOptions) for the synchronous variant
   */
  public CompletableFuture<List<ContactProperty>> listAsync(
      String listType, RequestOptions options) {
    Map<String, String> queryParams = new HashMap<>();
    if (listType != null) {
      queryParams.put("list", listType);
    }

    return sender
        .getListAsync(
            PROPERTIES_PATH,
            queryParams,
            new TypeReference<List<com.telos.loops.internal.openapi.model.ContactProperty>>() {},
            options)
        .thenApply(
            generatedProperties ->
                generatedProperties.stream()
                    .map(ContactPropertiesMapper::fromGenerated)
                    .collect(Collectors.toList()));
  }
}
