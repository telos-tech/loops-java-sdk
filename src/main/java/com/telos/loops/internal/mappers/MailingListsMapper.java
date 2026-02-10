package com.telos.loops.internal.mappers;

import com.telos.loops.lists.MailingList;

public class MailingListsMapper {

  // ============================================================
  // Response Mappings: Generated OpenAPI Models → Public API
  // ============================================================

  public static MailingList fromGenerated(
      com.telos.loops.internal.openapi.model.MailingList generated) {
    return new MailingList(
        generated.getId(),
        generated.getName(),
        generated.getDescription(),
        generated.getIsPublic());
  }
}
