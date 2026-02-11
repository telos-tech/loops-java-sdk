package com.telos.loops.transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Response containing a list of transactional email templates.
 *
 * <p>
 * This response includes pagination information for retrieving additional pages
 * of results.
 *
 * @param transactionalEmails the list of transactional email templates in this
 *                            page
 * @param pagination          pagination details for navigating through results
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record TransactionalListResponse(
    List<TransactionalEmail> transactionalEmails, Pagination pagination) {

  public TransactionalListResponse {
    transactionalEmails = transactionalEmails == null ? java.util.Collections.emptyList()
        : List.copyOf(transactionalEmails);
  }

  /**
   * Creates a new builder for {@link TransactionalListResponse}.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link TransactionalListResponse}. */
  public static final class Builder {
    private java.util.List<TransactionalEmail> transactionalEmails;
    private Pagination pagination;

    private Builder() {
    }

    public Builder transactionalEmails(List<TransactionalEmail> transactionalEmails) {
      this.transactionalEmails = transactionalEmails;
      return this;
    }

    public Builder addTransactionalEmail(TransactionalEmail transactionalEmail) {
      if (this.transactionalEmails == null) {
        this.transactionalEmails = new java.util.ArrayList<>();
      }
      this.transactionalEmails.add(transactionalEmail);
      return this;
    }

    public Builder pagination(Pagination pagination) {
      this.pagination = pagination;
      return this;
    }

    public TransactionalListResponse build() {
      return new TransactionalListResponse(transactionalEmails, pagination);
    }
  }

  /**
   * Pagination information for navigating through transactional email results.
   *
   * @param count      the total number of transactional emails in this page
   * @param nextCursor the cursor to use for retrieving the next page of results
   *                   (null if no more
   *                   pages)
   */
  @JsonIgnoreProperties(ignoreUnknown = false)
  public record Pagination(Integer count, String nextCursor) {
    public static Builder builder() {
      return new Builder();
    }

    public static final class Builder {
      private Integer count;
      private String nextCursor;

      private Builder() {
      }

      public Builder count(Integer count) {
        this.count = count;
        return this;
      }

      public Builder nextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
        return this;
      }

      public Pagination build() {
        return new Pagination(count, nextCursor);
      }
    }
  }
}
