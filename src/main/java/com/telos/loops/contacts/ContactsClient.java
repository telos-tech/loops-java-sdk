package com.telos.loops.contacts;

import com.fasterxml.jackson.core.type.TypeReference;
import com.telos.loops.internal.CoreSender;
import com.telos.loops.internal.mappers.ContactsMapper;
import com.telos.loops.model.RequestOptions;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Client for managing contacts in Loops.
 *
 * <p>This client provides full CRUD operations for contacts in your audience, including creating,
 * updating, finding, and deleting contacts. Contacts can be identified by email address or user ID.
 *
 * <h2>Example Usage</h2>
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * // Create a contact
 * ContactCreateRequest createRequest = new ContactCreateRequest(
 *     "user@example.com",
 *     null,  // userId (optional)
 *     "John Doe",  // firstName
 *     "Doe",  // lastName
 *     null,  // source
 *     true,  // subscribed
 *     null   // customProperties
 * );
 * ContactResponse response = client.contacts().create(createRequest);
 *
 * // Update a contact
 * ContactUpdateRequest updateRequest = new ContactUpdateRequest(
 *     "user@example.com",
 *     null,  // userId
 *     null,  // firstName
 *     null,  // lastName
 *     "Newsletter",  // mailingLists (comma-separated)
 *     null,  // subscribed
 *     null   // customProperties
 * );
 * client.contacts().update(updateRequest);
 *
 * // Find contacts
 * ContactFindRequest findRequest = new ContactFindRequest("user@example.com", null);
 * List<Contact> contacts = client.contacts().find(findRequest);
 *
 * // Delete a contact
 * ContactDeleteRequest deleteRequest = new ContactDeleteRequest("user@example.com", null);
 * client.contacts().delete(deleteRequest);
 *
 * // Async example
 * CompletableFuture<ContactResponse> future =
 *     client.contacts().createAsync(createRequest);
 * }</pre>
 *
 * <p>All methods are available in both synchronous and asynchronous (CompletableFuture) variants.
 *
 * @see com.telos.loops.LoopsClient
 */
public class ContactsClient {
  private static final String CREATE_PATH = "/contacts/create";
  private static final String UPDATE_PATH = "/contacts/update";
  private static final String FIND_PATH = "/contacts/find";
  private static final String DELETE_PATH = "/contacts/delete";

  private final CoreSender sender;

  public ContactsClient(CoreSender sender) {
    this.sender = Objects.requireNonNull(sender);
  }

  // ============================================================
  // Create Operations
  // ============================================================

  /**
   * Creates a new contact in Loops.
   *
   * <p>This method uses default request options.
   *
   * @param request the request object containing contact details
   * @return the response containing the created contact's status
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if the request fails validation
   * @see #createAsync(ContactCreateRequest) for the asynchronous variant
   */
  public ContactResponse create(ContactCreateRequest request) {
    return create(request, RequestOptions.none());
  }

  /**
   * Creates a new contact in Loops with custom request options.
   *
   * @param request the request object containing contact details
   * @param options additional request options such as custom headers
   * @return the response containing the created contact's status
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if the request fails validation
   * @see #createAsync(ContactCreateRequest, RequestOptions) for the asynchronous variant
   */
  public ContactResponse create(ContactCreateRequest request, RequestOptions options) {
    var generatedRequest = ContactsMapper.toGenerated(request);
    var generatedResponse =
        sender.postJson(
            CREATE_PATH,
            generatedRequest,
            com.telos.loops.internal.openapi.model.ContactSuccessResponse.class,
            options);
    return ContactsMapper.fromGenerated(generatedResponse);
  }

  /**
   * Asynchronously creates a new contact in Loops.
   *
   * <p>This method uses default request options.
   *
   * @param request the request object containing contact details
   * @return a CompletableFuture containing the created contact's status
   * @see #create(ContactCreateRequest) for the synchronous variant
   */
  public CompletableFuture<ContactResponse> createAsync(ContactCreateRequest request) {
    return createAsync(request, RequestOptions.none());
  }

  /**
   * Asynchronously creates a new contact in Loops with custom request options.
   *
   * @param request the request object containing contact details
   * @param options additional request options such as custom headers
   * @return a CompletableFuture containing the created contact's status
   * @see #create(ContactCreateRequest, RequestOptions) for the synchronous variant
   */
  public CompletableFuture<ContactResponse> createAsync(
      ContactCreateRequest request, RequestOptions options) {
    var generatedRequest = ContactsMapper.toGenerated(request);
    return sender
        .postJsonAsync(
            CREATE_PATH,
            generatedRequest,
            com.telos.loops.internal.openapi.model.ContactSuccessResponse.class,
            options)
        .thenApply(ContactsMapper::fromGenerated);
  }

  // ============================================================
  // Update Operations
  // ============================================================

  /**
   * Updates an existing contact in Loops.
   *
   * <p>This method uses default request options.
   *
   * @param request the request object containing updated contact details
   * @return the response containing the updated contact's status
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if the request fails validation
   * @see #updateAsync(ContactUpdateRequest) for the asynchronous variant
   */
  public ContactResponse update(ContactUpdateRequest request) {
    return update(request, RequestOptions.none());
  }

  /**
   * Updates an existing contact in Loops with custom request options.
   *
   * @param request the request object containing updated contact details
   * @param options additional request options such as custom headers
   * @return the response containing the updated contact's status
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if the request fails validation
   * @see #updateAsync(ContactUpdateRequest, RequestOptions) for the asynchronous variant
   */
  public ContactResponse update(ContactUpdateRequest request, RequestOptions options) {
    var generatedRequest = ContactsMapper.toGenerated(request);
    var generatedResponse =
        sender.putJson(
            UPDATE_PATH,
            generatedRequest,
            com.telos.loops.internal.openapi.model.ContactSuccessResponse.class,
            options);
    return ContactsMapper.fromGenerated(generatedResponse);
  }

  /**
   * Asynchronously updates an existing contact in Loops.
   *
   * <p>This method uses default request options.
   *
   * @param request the request object containing updated contact details
   * @return a CompletableFuture containing the updated contact's status
   * @see #update(ContactUpdateRequest) for the synchronous variant
   */
  public CompletableFuture<ContactResponse> updateAsync(ContactUpdateRequest request) {
    return updateAsync(request, RequestOptions.none());
  }

  /**
   * Asynchronously updates an existing contact in Loops with custom request options.
   *
   * @param request the request object containing updated contact details
   * @param options additional request options such as custom headers
   * @return a CompletableFuture containing the updated contact's status
   * @see #update(ContactUpdateRequest, RequestOptions) for the synchronous variant
   */
  public CompletableFuture<ContactResponse> updateAsync(
      ContactUpdateRequest request, RequestOptions options) {
    var generatedRequest = ContactsMapper.toGenerated(request);
    return sender
        .putJsonAsync(
            UPDATE_PATH,
            generatedRequest,
            com.telos.loops.internal.openapi.model.ContactSuccessResponse.class,
            options)
        .thenApply(ContactsMapper::fromGenerated);
  }

  // ============================================================
  // Find Operations
  // ============================================================

  /**
   * Finds contacts in Loops by email address or user ID.
   *
   * <p>This method uses default request options.
   *
   * @param request the request object containing search criteria (email or userId)
   * @return a list of matching contacts
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if the request fails validation
   * @see #findAsync(ContactFindRequest) for the asynchronous variant
   */
  public List<Contact> find(ContactFindRequest request) {
    return find(request, RequestOptions.none());
  }

  /**
   * Finds contacts in Loops by email address or user ID with custom request options.
   *
   * @param request the request object containing search criteria (email or userId)
   * @param options additional request options such as custom headers
   * @return a list of matching contacts
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if the request fails validation
   * @see #findAsync(ContactFindRequest, RequestOptions) for the asynchronous variant
   */
  public List<Contact> find(ContactFindRequest request, RequestOptions options) {
    var queryParams = ContactsMapper.toQueryParams(request);
    var generatedContacts =
        sender.getList(
            FIND_PATH,
            queryParams,
            new TypeReference<List<com.telos.loops.internal.openapi.model.Contact>>() {},
            options);
    return generatedContacts.stream()
        .map(ContactsMapper::fromGenerated)
        .collect(Collectors.toList());
  }

  /**
   * Asynchronously finds contacts in Loops by email address or user ID.
   *
   * <p>This method uses default request options.
   *
   * @param request the request object containing search criteria (email or userId)
   * @return a CompletableFuture containing a list of matching contacts
   * @see #find(ContactFindRequest) for the synchronous variant
   */
  public CompletableFuture<List<Contact>> findAsync(ContactFindRequest request) {
    return findAsync(request, RequestOptions.none());
  }

  /**
   * Asynchronously finds contacts in Loops by email address or user ID with custom request options.
   *
   * @param request the request object containing search criteria (email or userId)
   * @param options additional request options such as custom headers
   * @return a CompletableFuture containing a list of matching contacts
   * @see #find(ContactFindRequest, RequestOptions) for the synchronous variant
   */
  public CompletableFuture<List<Contact>> findAsync(
      ContactFindRequest request, RequestOptions options) {
    var queryParams = ContactsMapper.toQueryParams(request);
    return sender
        .getListAsync(
            FIND_PATH,
            queryParams,
            new TypeReference<List<com.telos.loops.internal.openapi.model.Contact>>() {},
            options)
        .thenApply(
            generatedContacts ->
                generatedContacts.stream()
                    .map(ContactsMapper::fromGenerated)
                    .collect(Collectors.toList()));
  }

  // ============================================================
  // Delete Operations
  // ============================================================

  /**
   * Deletes a contact from Loops by email address or user ID.
   *
   * <p>This method uses default request options.
   *
   * @param request the request object containing the contact identifier (email or userId)
   * @return the response containing the deletion status
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if the request fails validation
   * @see #deleteAsync(ContactDeleteRequest) for the asynchronous variant
   */
  public ContactResponse delete(ContactDeleteRequest request) {
    return delete(request, RequestOptions.none());
  }

  /**
   * Deletes a contact from Loops by email address or user ID with custom request options.
   *
   * @param request the request object containing the contact identifier (email or userId)
   * @param options additional request options such as custom headers
   * @return the response containing the deletion status
   * @throws com.telos.loops.error.LoopsApiException if the API returns an error
   * @throws com.telos.loops.error.LoopsValidationException if the request fails validation
   * @see #deleteAsync(ContactDeleteRequest, RequestOptions) for the asynchronous variant
   */
  public ContactResponse delete(ContactDeleteRequest request, RequestOptions options) {
    var generatedRequest = ContactsMapper.toGenerated(request);
    var generatedResponse =
        sender.deleteJson(
            DELETE_PATH,
            generatedRequest,
            com.telos.loops.internal.openapi.model.ContactDeleteResponse.class,
            options);
    return ContactsMapper.fromGenerated(generatedResponse);
  }

  /**
   * Asynchronously deletes a contact from Loops by email address or user ID.
   *
   * <p>This method uses default request options.
   *
   * @param request the request object containing the contact identifier (email or userId)
   * @return a CompletableFuture containing the deletion status
   * @see #delete(ContactDeleteRequest) for the synchronous variant
   */
  public CompletableFuture<ContactResponse> deleteAsync(ContactDeleteRequest request) {
    return deleteAsync(request, RequestOptions.none());
  }

  /**
   * Asynchronously deletes a contact from Loops by email address or user ID with custom request
   * options.
   *
   * @param request the request object containing the contact identifier (email or userId)
   * @param options additional request options such as custom headers
   * @return a CompletableFuture containing the deletion status
   * @see #delete(ContactDeleteRequest, RequestOptions) for the synchronous variant
   */
  public CompletableFuture<ContactResponse> deleteAsync(
      ContactDeleteRequest request, RequestOptions options) {
    var generatedRequest = ContactsMapper.toGenerated(request);
    return sender
        .deleteJsonAsync(
            DELETE_PATH,
            generatedRequest,
            com.telos.loops.internal.openapi.model.ContactDeleteResponse.class,
            options)
        .thenApply(ContactsMapper::fromGenerated);
  }
}
