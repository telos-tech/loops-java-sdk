package com.telos.loops.transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telos.loops.error.LoopsValidationException;
import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = false)
public record TransactionalSendRequest(
    @Nonnull String transactionalId,
    @Nonnull String email,
    Boolean addToAudience,
    Map<String, Object> dataVariables,
    List<Attachment> attachments) {

  public TransactionalSendRequest {
    if (transactionalId == null || transactionalId.isBlank()) {
      throw new LoopsValidationException("transactionalId is required and cannot be blank");
    }
    if (email == null || email.isBlank()) {
      throw new LoopsValidationException("email is required and cannot be blank");
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = false)
  public record Attachment(String filename, String contentType, String data) {}
}
