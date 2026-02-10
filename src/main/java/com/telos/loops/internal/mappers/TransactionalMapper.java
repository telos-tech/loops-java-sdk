package com.telos.loops.internal.mappers;

import com.telos.loops.transactional.TransactionalEmail;
import com.telos.loops.transactional.TransactionalListResponse;
import com.telos.loops.transactional.TransactionalResponse;
import com.telos.loops.transactional.TransactionalSendRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionalMapper {

  // ============================================================
  // Request Mappings: Public API → Generated OpenAPI Models
  // ============================================================

  public static com.telos.loops.internal.openapi.model.TransactionalRequest toGenerated(
      TransactionalSendRequest request) {
    var generated = new com.telos.loops.internal.openapi.model.TransactionalRequest();
    generated.setTransactionalId(request.transactionalId());
    generated.setEmail(request.email());
    generated.setAddToAudience(request.addToAudience());
    generated.setDataVariables(request.dataVariables());

    // Map attachments
    if (request.attachments() != null) {
      var generatedAttachments =
          request.attachments().stream()
              .map(
                  att -> {
                    var genAtt =
                        new com.telos.loops.internal.openapi.model
                            .TransactionalRequestAttachmentsInner();
                    genAtt.setFilename(att.filename());
                    genAtt.setContentType(att.contentType());
                    genAtt.setData(att.data());
                    return genAtt;
                  })
              .collect(Collectors.toList());
      generated.setAttachments(generatedAttachments);
    }

    return generated;
  }

  // ============================================================
  // Response Mappings: Generated OpenAPI Models → Public API
  // ============================================================

  public static TransactionalResponse fromGenerated(
      com.telos.loops.internal.openapi.model.TransactionalSuccessResponse response) {
    return new TransactionalResponse(response.getSuccess());
  }

  public static TransactionalEmail fromGenerated(
      com.telos.loops.internal.openapi.model.TransactionalEmail generated) {
    return new TransactionalEmail(
        generated.getId(),
        generated.getName(),
        generated.getLastUpdated(),
        generated.getDataVariables());
  }

  public static TransactionalListResponse fromGenerated(
      com.telos.loops.internal.openapi.model.ListTransactionalsResponse generated) {
    List<TransactionalEmail> emails = new ArrayList<>();
    if (generated.getData() != null) {
      emails =
          generated.getData().stream()
              .map(TransactionalMapper::fromGenerated)
              .collect(Collectors.toList());
    }

    TransactionalListResponse.Pagination pagination = null;
    if (generated.getPagination() != null) {
      var genPagination = generated.getPagination();
      Integer count =
          genPagination.getTotalResults() != null
              ? genPagination.getTotalResults().intValue()
              : null;
      pagination = new TransactionalListResponse.Pagination(count, genPagination.getNextCursor());
    }

    return new TransactionalListResponse(emails, pagination);
  }
}
