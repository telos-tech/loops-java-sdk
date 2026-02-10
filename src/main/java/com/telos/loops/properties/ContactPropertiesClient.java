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

public class ContactPropertiesClient {
  private static final String PROPERTIES_PATH = "/contacts/properties";

  private final CoreSender sender;

  public ContactPropertiesClient(CoreSender sender) {
    this.sender = Objects.requireNonNull(sender);
  }

  // ============================================================
  // Create Operations
  // ============================================================

  public ContactPropertyResponse create(ContactPropertyCreateRequest request) {
    return create(request, RequestOptions.none());
  }

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

  public CompletableFuture<ContactPropertyResponse> createAsync(
      ContactPropertyCreateRequest request) {
    return createAsync(request, RequestOptions.none());
  }

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

  public List<ContactProperty> list() {
    return list(null, RequestOptions.none());
  }

  public List<ContactProperty> list(String listType) {
    return list(listType, RequestOptions.none());
  }

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

  public CompletableFuture<List<ContactProperty>> listAsync() {
    return listAsync(null, RequestOptions.none());
  }

  public CompletableFuture<List<ContactProperty>> listAsync(String listType) {
    return listAsync(listType, RequestOptions.none());
  }

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
