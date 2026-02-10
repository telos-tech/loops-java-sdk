package com.telos.loops.contacts;

import com.fasterxml.jackson.core.type.TypeReference;
import com.telos.loops.internal.CoreSender;
import com.telos.loops.internal.mappers.ContactsMapper;
import com.telos.loops.model.RequestOptions;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

  public ContactResponse create(ContactCreateRequest request) {
    return create(request, RequestOptions.none());
  }

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

  public CompletableFuture<ContactResponse> createAsync(ContactCreateRequest request) {
    return createAsync(request, RequestOptions.none());
  }

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

  public ContactResponse update(ContactUpdateRequest request) {
    return update(request, RequestOptions.none());
  }

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

  public CompletableFuture<ContactResponse> updateAsync(ContactUpdateRequest request) {
    return updateAsync(request, RequestOptions.none());
  }

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

  public List<Contact> find(ContactFindRequest request) {
    return find(request, RequestOptions.none());
  }

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

  public CompletableFuture<List<Contact>> findAsync(ContactFindRequest request) {
    return findAsync(request, RequestOptions.none());
  }

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

  public ContactResponse delete(ContactDeleteRequest request) {
    return delete(request, RequestOptions.none());
  }

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

  public CompletableFuture<ContactResponse> deleteAsync(ContactDeleteRequest request) {
    return deleteAsync(request, RequestOptions.none());
  }

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
