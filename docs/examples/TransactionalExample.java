package com.telos.loops.examples;

import com.telos.loops.LoopsClient;
import com.telos.loops.error.LoopsApiException;
import com.telos.loops.transactional.TransactionalEmail;
import com.telos.loops.transactional.TransactionalListResponse;
import com.telos.loops.transactional.TransactionalResponse;
import com.telos.loops.transactional.TransactionalSendRequest;
import com.telos.loops.transactional.TransactionalSendRequest.Attachment;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Transactional Email Example - Comprehensive Transactional Email Operations
 *
 * <p>This example demonstrates all aspects of sending transactional emails:
 * <ul>
 *   <li>Sending basic transactional emails</li>
 *   <li>Using data variables for dynamic content</li>
 *   <li>Including attachments (PDFs, images, documents)</li>
 *   <li>Implementing idempotency to prevent duplicate sends</li>
 *   <li>Listing transactional email templates</li>
 *   <li>Pagination through templates</li>
 *   <li>Adding recipients to audience during send</li>
 *   <li>Common use cases (receipts, notifications, password resets)</li>
 * </ul>
 *
 * <p>Transactional emails are one-time messages triggered by user actions or system events,
 * such as order confirmations, password resets, account notifications, etc.
 *
 * <p><b>Prerequisites:</b>
 * <ul>
 *   <li>Set the LOOPS_API_KEY environment variable</li>
 *   <li>Create at least one transactional email template in Loops</li>
 *   <li>Note the transactional template ID from your Loops dashboard</li>
 * </ul>
 *
 * <p><b>To run this example:</b>
 * <pre>
 * export LOOPS_API_KEY="your-api-key-here"
 * java com.telos.loops.examples.TransactionalExample
 * </pre>
 */
public class TransactionalExample {

  // Replace with your actual transactional template ID from Loops dashboard
  private static final String TEMPLATE_ID = "cly8kzmsp000gl50oyf5qaj0h";

  public static void main(String[] args) {
    String apiKey = System.getenv("LOOPS_API_KEY");
    if (apiKey == null || apiKey.isBlank()) {
      System.err.println("Error: LOOPS_API_KEY environment variable is not set");
      System.exit(1);
    }

    try {
      LoopsClient client = LoopsClient.builder()
          .apiKey(apiKey)
          .build();

      // ========================================
      // Example 1: Basic Transactional Email
      // ========================================
      System.out.println("=== Example 1: Sending a Basic Transactional Email ===");

      // The simplest transactional send requires only template ID and recipient email
      TransactionalSendRequest basicRequest = new TransactionalSendRequest(
          TEMPLATE_ID,              // Your template ID from Loops
          "recipient@example.com",  // Recipient email address
          false,                    // Don't add to audience
          null,                     // No data variables
          null                      // No attachments
      );

      TransactionalResponse basicResponse = client.transactional().send(basicRequest);
      System.out.println("Basic transactional email sent: " + basicResponse.success());

      // ========================================
      // Example 2: Email with Data Variables
      // ========================================
      System.out.println("\n=== Example 2: Using Data Variables ===");

      // Data variables personalize the email content
      // Reference these in your template using {{variable_name}}
      Map<String, Object> dataVariables = new HashMap<>();
      dataVariables.put("user_name", "Alice Johnson");
      dataVariables.put("company_name", "Acme Corporation");
      dataVariables.put("login_url", "https://app.example.com/login");
      dataVariables.put("support_email", "support@example.com");

      TransactionalSendRequest variablesRequest = new TransactionalSendRequest(
          TEMPLATE_ID,
          "alice@example.com",
          false,
          dataVariables,  // Include personalization data
          null
      );

      TransactionalResponse variablesResponse = client.transactional().send(variablesRequest);
      System.out.println("Email with variables sent: " + variablesResponse.success());

      // ========================================
      // Example 3: Adding Recipient to Audience
      // ========================================
      System.out.println("\n=== Example 3: Adding Recipient to Audience ===");

      // Setting addToAudience to true creates a contact if one doesn't exist
      // This is useful for building your audience while sending transactional emails
      Map<String, Object> onboardingData = new HashMap<>();
      onboardingData.put("user_name", "Bob Smith");
      onboardingData.put("trial_days", 14);
      onboardingData.put("features_enabled", "all");

      TransactionalSendRequest audienceRequest = new TransactionalSendRequest(
          TEMPLATE_ID,
          "bob@example.com",
          true,  // Add to audience if not already present
          onboardingData,
          null
      );

      TransactionalResponse audienceResponse = client.transactional().send(audienceRequest);
      System.out.println("Email sent and recipient added to audience: " + audienceResponse.success());

      // ========================================
      // Example 4: Email with Attachments
      // ========================================
      System.out.println("\n=== Example 4: Sending Attachments ===");

      // Attachments must be base64-encoded
      // Common use cases: invoices, receipts, reports, tickets

      // Example: Create a simple text file attachment
      String fileContent = "Thank you for your purchase!\nOrder #12345\nTotal: $99.99";
      String base64Content = Base64.getEncoder()
          .encodeToString(fileContent.getBytes(StandardCharsets.UTF_8));

      Attachment receiptAttachment = new Attachment(
          "receipt.txt",           // Filename
          "text/plain",            // MIME type
          base64Content            // Base64-encoded content
      );

      // Example: Simulate a PDF invoice (in real use, encode actual PDF bytes)
      String pdfSimulation = "PDF content would go here";
      String base64Pdf = Base64.getEncoder()
          .encodeToString(pdfSimulation.getBytes(StandardCharsets.UTF_8));

      Attachment invoiceAttachment = new Attachment(
          "invoice.pdf",
          "application/pdf",
          base64Pdf
      );

      List<Attachment> attachments = new ArrayList<>();
      attachments.add(receiptAttachment);
      attachments.add(invoiceAttachment);

      Map<String, Object> purchaseData = new HashMap<>();
      purchaseData.put("customer_name", "Charlie Davis");
      purchaseData.put("order_id", "ORD-12345");
      purchaseData.put("amount", 99.99);
      purchaseData.put("purchase_date", Instant.now().toString());

      TransactionalSendRequest attachmentRequest = new TransactionalSendRequest(
          TEMPLATE_ID,
          "charlie@example.com",
          false,
          purchaseData,
          attachments  // Include attachments
      );

      TransactionalResponse attachmentResponse = client.transactional().send(attachmentRequest);
      System.out.println("Email with attachments sent: " + attachmentResponse.success());

      // ========================================
      // Example 5: Idempotency for Critical Emails
      // ========================================
      System.out.println("\n=== Example 5: Using Idempotency Keys ===");

      // Idempotency prevents duplicate sends in case of network issues or retries
      // Use a unique key based on the logical event (e.g., order ID + email type)

      String orderId = "ORD-98765";
      String idempotencyKey = "order_confirmation_" + orderId;

      Map<String, Object> orderData = new HashMap<>();
      orderData.put("customer_name", "Diana Prince");
      orderData.put("order_id", orderId);
      orderData.put("total", 249.99);
      orderData.put("items_count", 3);
      orderData.put("estimated_delivery", "2024-02-15");

      TransactionalSendRequest orderRequest = new TransactionalSendRequest(
          TEMPLATE_ID,
          "diana@example.com",
          false,
          orderData,
          null
      );

      // First send with idempotency key
      TransactionalResponse firstSend = client.transactional().send(orderRequest, idempotencyKey);
      System.out.println("Order confirmation sent: " + firstSend.success());

      // Attempt to send again with the same key (simulating a retry)
      // Loops will recognize this as a duplicate and handle it gracefully
      TransactionalResponse duplicateSend = client.transactional().send(orderRequest, idempotencyKey);
      System.out.println("Duplicate send handled: " + duplicateSend.success());
      System.out.println("Note: Idempotency prevents sending the same email twice");

      // ========================================
      // Example 6: Common Use Cases
      // ========================================
      System.out.println("\n=== Example 6: Common Transactional Email Use Cases ===");

      // Use Case 1: Password Reset
      System.out.println("\n--- Password Reset Email ---");
      String resetToken = UUID.randomUUID().toString();
      Map<String, Object> resetData = new HashMap<>();
      resetData.put("user_name", "Eva Martinez");
      resetData.put("reset_link", "https://app.example.com/reset?token=" + resetToken);
      resetData.put("expiration_hours", 24);

      TransactionalSendRequest resetRequest = new TransactionalSendRequest(
          TEMPLATE_ID,
          "eva@example.com",
          false,
          resetData,
          null
      );

      TransactionalResponse resetResponse = client.transactional().send(resetRequest);
      System.out.println("Password reset email sent: " + resetResponse.success());

      // Use Case 2: Account Verification
      System.out.println("\n--- Account Verification Email ---");
      String verificationCode = String.format("%06d", (int) (Math.random() * 1000000));
      Map<String, Object> verificationData = new HashMap<>();
      verificationData.put("user_name", "Frank Wilson");
      verificationData.put("verification_code", verificationCode);
      verificationData.put("code_expiry_minutes", 10);

      TransactionalSendRequest verificationRequest = new TransactionalSendRequest(
          TEMPLATE_ID,
          "frank@example.com",
          true,  // Add to audience on verification
          verificationData,
          null
      );

      TransactionalResponse verificationResponse = client.transactional().send(verificationRequest);
      System.out.println("Verification email sent: " + verificationResponse.success());

      // Use Case 3: Subscription Confirmation
      System.out.println("\n--- Subscription Confirmation ---");
      Map<String, Object> subscriptionData = new HashMap<>();
      subscriptionData.put("subscriber_name", "Grace Lee");
      subscriptionData.put("plan_name", "Pro Annual");
      subscriptionData.put("price", 299.99);
      subscriptionData.put("billing_date", "2024-02-10");
      subscriptionData.put("next_billing_date", "2025-02-10");

      TransactionalSendRequest subscriptionRequest = new TransactionalSendRequest(
          TEMPLATE_ID,
          "grace@example.com",
          false,
          subscriptionData,
          null
      );

      String subscriptionKey = "subscription_" + UUID.randomUUID();
      TransactionalResponse subscriptionResponse = client.transactional()
          .send(subscriptionRequest, subscriptionKey);
      System.out.println("Subscription confirmation sent: " + subscriptionResponse.success());

      // ========================================
      // Example 7: Listing Transactional Email Templates
      // ========================================
      System.out.println("\n=== Example 7: Listing Transactional Templates ===");

      // List recently sent transactional emails
      // Default pagination: 10 items per page
      TransactionalListResponse listResponse = client.transactional().list();

      System.out.println("Found " + listResponse.transactionalEmails().size() + " transactional email template(s)");
      for (TransactionalEmail email : listResponse.transactionalEmails()) {
        System.out.println("\nEmail Template:");
        System.out.println("  ID: " + email.id());
        System.out.println("  Name: " + email.name());
        System.out.println("  Last Updated: " + email.lastUpdated());
        System.out.println("  Data Variables: " + email.dataVariables());
      }

      // Display pagination info
      if (listResponse.pagination() != null) {
        System.out.println("\nPagination:");
        System.out.println("  Count: " + listResponse.pagination().count());
        System.out.println("  Next Cursor: " + listResponse.pagination().nextCursor());
      }

      // ========================================
      // Example 8: Pagination
      // ========================================
      System.out.println("\n=== Example 8: Paginating Through Templates ===");

      // Request specific page size (between 10 and 50)
      int perPage = 20;
      TransactionalListResponse firstPage = client.transactional().list(perPage, null);

      System.out.println("First page contains " + firstPage.transactionalEmails().size() + " template(s)");

      if (firstPage.pagination() != null) {
        System.out.println("Pagination info:");
        System.out.println("  Count: " + firstPage.pagination().count());
        System.out.println("  Next Cursor: " + firstPage.pagination().nextCursor());

        // If there's a next cursor, fetch the next page
        String nextCursor = firstPage.pagination().nextCursor();
        if (nextCursor != null && !nextCursor.isEmpty()) {
          TransactionalListResponse secondPage = client.transactional().list(perPage, nextCursor);
          System.out.println("\nSecond page contains " + secondPage.transactionalEmails().size() + " template(s)");

          // Continue pagination while there are more pages
          int pageCount = 2;
          TransactionalListResponse currentPage = secondPage;
          while (currentPage.pagination() != null
              && currentPage.pagination().nextCursor() != null
              && !currentPage.pagination().nextCursor().isEmpty()
              && pageCount < 5) {
            currentPage = client.transactional().list(perPage, currentPage.pagination().nextCursor());
            pageCount++;
            System.out.println("Page " + pageCount + " contains " + currentPage.transactionalEmails().size() + " template(s)");
          }
        } else {
          System.out.println("All templates fit on first page");
        }
      }

      System.out.println("\n=== Transactional Email Example Complete ===");
      System.out.println("Best Practices:");
      System.out.println("- Use idempotency keys for critical emails (orders, payments)");
      System.out.println("- Include all necessary data variables in each send");
      System.out.println("- Set addToAudience=true to grow your contact list");
      System.out.println("- Keep attachments under 3MB total size");
      System.out.println("- Use proper MIME types for attachments");

    } catch (LoopsApiException e) {
      System.err.println("\nAPI Error:");
      System.err.println("Status: " + e.statusCode());
      System.err.println("Message: " + e.getMessage());
      if (e.error() != null) {
        System.err.println("Error Details: " + e.error());
      }
      e.printStackTrace();
      System.exit(1);

    } catch (Exception e) {
      System.err.println("\nUnexpected error:");
      System.err.println("Type: " + e.getClass().getSimpleName());
      System.err.println("Message: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
}
