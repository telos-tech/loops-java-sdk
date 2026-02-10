package com.telos.loops.transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = false)
public record TransactionalListResponse(
    List<TransactionalEmail> transactionalEmails, Pagination pagination) {

  @JsonIgnoreProperties(ignoreUnknown = false)
  public record Pagination(Integer count, String nextCursor) {}
}
