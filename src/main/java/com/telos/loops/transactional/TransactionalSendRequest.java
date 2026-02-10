package com.telos.loops.transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telos.loops.error.LoopsValidationException;
import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * Request to send a transactional email via Loops.
 *
 * <p>
 * Transactional emails are sent immediately to a specific recipient. You must
 * provide the email
 * template ID and recipient email address. Dynamic data can be included via
 * dataVariables to
 * personalize the email content.
 *
 * <p>
 * Example usage:
 *
 * <pre>{@code
 * TransactionalSendRequest request = new TransactionalSendRequest(
 *     "clxyz123abc", // transactionalId (required)
 *     "user@example.com", // email (required)
 *     true, // addToAudience
 *     Map.of("resetLink", "https://example.com/reset/token123"), // dataVariables
 *     List.of(new Attachment("invoice.pdf", "application/pdf", "base64data...")) // attachments
 * );
 * }</pre>
 *
 * @param transactionalId the ID of the transactional email template to send
 *                        (required)
 * @param email           the recipient's email address (required)
 * @param addToAudience   whether to add this recipient to your audience if they
 *                        don't exist
 * @param dataVariables   map of variable names to values for template
 *                        personalization
 * @param attachments     list of file attachments to include with the email
 * @throws LoopsValidationException if transactionalId or email is null or blank
 */
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
    dataVariables = dataVariables == null ? java.util.Collections.emptyMap() : Map.copyOf(dataVariables);
    attachments = attachments == null ? java.util.Collections.emptyList() : List.copyOf(attachments);
  }

  /**
   * Creates a new builder for {@link TransactionalSendRequest}.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link TransactionalSendRequest}. */
  public static final class Builder {
    private String transactionalId;
    private String email;
    private Boolean addToAudience;
    private java.util.Map<String, Object> dataVariables;
    private java.util.List<Attachment> attachments;

    private Builder() {
    }

    public Builder transactionalId(String transactionalId) {
      this.transactionalId = transactionalId;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder addToAudience(Boolean addToAudience) {
      this.addToAudience = addToAudience;
      return this;
    }

    public Builder dataVariables(Map<String, Object> dataVariables) {
      this.dataVariables = dataVariables;
      return this;
    }

    public Builder putDataVariable(String key, Object value) {
      if (this.dataVariables == null) {
        this.dataVariables = new java.util.HashMap<>();
      }
      this.dataVariables.put(key, value);
      return this;
    }

    public Builder attachments(List<Attachment> attachments) {
      this.attachments = attachments;
      return this;
    }

    public Builder addAttachment(Attachment attachment) {
      if (this.attachments == null) {
        this.attachments = new java.util.ArrayList<>();
      }
      this.attachments.add(attachment);
      return this;
    }

    public TransactionalSendRequest build() {
      return new TransactionalSendRequest(
          transactionalId, email, addToAudience, dataVariables, attachments);
    }
  }

  /**
   * Represents a file attachment for a transactional email.
   *
   * @param filename    the name of the file (e.g., "invoice.pdf")
   * @param contentType the MIME type of the file (e.g., "application/pdf",
   *                    "image/png")
   * @param data        the base64-encoded file data
   */
  @JsonIgnoreProperties(ignoreUnknown = false)
  public record Attachment(String filename, String contentType, String data) {
    /**
     * Creates a new builder for {@link Attachment}.
     *
     * @return a new builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Builder for {@link Attachment}. */
    public static final class Builder {
      private String filename;
      private String contentType;
      private String data;

      private Builder() {
      }

      public Builder filename(String filename) {
        this.filename = filename;
        return this;
      }

      public Builder contentType(String contentType) {
        this.contentType = contentType;
        return this;
      }

      public Builder data(String data) {
        this.data = data;
        return this;
      }

      public Attachment build() {
        return new Attachment(filename, contentType, data);
      }
    }
  }
}
