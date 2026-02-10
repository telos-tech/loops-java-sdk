package com.telos.loops.transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telos.loops.error.LoopsValidationException;
import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * Request to send a transactional email via Loops.
 *
 * <p>Transactional emails are sent immediately to a specific recipient. You must provide the email
 * template ID and recipient email address. Dynamic data can be included via dataVariables to
 * personalize the email content.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * TransactionalSendRequest request = new TransactionalSendRequest(
 *     "clxyz123abc",                 // transactionalId (required)
 *     "user@example.com",            // email (required)
 *     true,                          // addToAudience
 *     Map.of("resetLink", "https://example.com/reset/token123"),  // dataVariables
 *     List.of(new Attachment("invoice.pdf", "application/pdf", "base64data..."))  // attachments
 * );
 * }</pre>
 *
 * @param transactionalId the ID of the transactional email template to send (required)
 * @param email the recipient's email address (required)
 * @param addToAudience whether to add this recipient to your audience if they don't exist
 * @param dataVariables map of variable names to values for template personalization
 * @param attachments list of file attachments to include with the email
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
  }

  /**
   * Represents a file attachment for a transactional email.
   *
   * @param filename the name of the file (e.g., "invoice.pdf")
   * @param contentType the MIME type of the file (e.g., "application/pdf", "image/png")
   * @param data the base64-encoded file data
   */
  @JsonIgnoreProperties(ignoreUnknown = false)
  public record Attachment(String filename, String contentType, String data) {}
}
