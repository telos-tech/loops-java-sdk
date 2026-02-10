package com.telos.loops.transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Response containing a list of transactional email templates.
 *
 * <p>This response includes pagination information for retrieving additional pages of results.
 *
 * @param transactionalEmails the list of transactional email templates in this page
 * @param pagination pagination details for navigating through results
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record TransactionalListResponse(
    List<TransactionalEmail> transactionalEmails, Pagination pagination) {

  /**
   * Pagination information for navigating through transactional email results.
   *
   * @param count the total number of transactional emails in this page
   * @param nextCursor the cursor to use for retrieving the next page of results (null if no more
   *     pages)
   */
  @JsonIgnoreProperties(ignoreUnknown = false)
  public record Pagination(Integer count, String nextCursor) {}
}
