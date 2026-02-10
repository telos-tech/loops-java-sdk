package com.telos.loops.lists;

import com.fasterxml.jackson.core.type.TypeReference;
import com.telos.loops.internal.CoreSender;
import com.telos.loops.internal.mappers.MailingListsMapper;
import com.telos.loops.model.RequestOptions;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MailingListsClient {
  private static final String LIST_PATH = "/lists";

  private final CoreSender sender;

  public MailingListsClient(CoreSender sender) {
    this.sender = Objects.requireNonNull(sender);
  }

  // ============================================================
  // List Operations
  // ============================================================

  public List<MailingList> list() {
    return list(RequestOptions.none());
  }

  public List<MailingList> list(RequestOptions options) {
    var generatedLists =
        sender.getList(
            LIST_PATH,
            new HashMap<>(),
            new TypeReference<List<com.telos.loops.internal.openapi.model.MailingList>>() {},
            options);
    return generatedLists.stream()
        .map(MailingListsMapper::fromGenerated)
        .collect(Collectors.toList());
  }

  public CompletableFuture<List<MailingList>> listAsync() {
    return listAsync(RequestOptions.none());
  }

  public CompletableFuture<List<MailingList>> listAsync(RequestOptions options) {
    return sender
        .getListAsync(
            LIST_PATH,
            new HashMap<>(),
            new TypeReference<List<com.telos.loops.internal.openapi.model.MailingList>>() {},
            options)
        .thenApply(
            generatedLists ->
                generatedLists.stream()
                    .map(MailingListsMapper::fromGenerated)
                    .collect(Collectors.toList()));
  }
}
