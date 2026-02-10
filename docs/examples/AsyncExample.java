package com.telos.loops.examples;

import com.telos.loops.LoopsClient;
import com.telos.loops.contacts.Contact;
import com.telos.loops.contacts.ContactCreateRequest;
import com.telos.loops.contacts.ContactFindRequest;
import com.telos.loops.contacts.ContactResponse;
import com.telos.loops.error.LoopsApiException;
import com.telos.loops.events.EventResponse;
import com.telos.loops.events.EventSendRequest;
import com.telos.loops.lists.MailingList;
import com.telos.loops.transactional.TransactionalResponse;
import com.telos.loops.transactional.TransactionalSendRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * Async Operations Example - Advanced Asynchronous Patterns
 *
 * <p>This example demonstrates comprehensive asynchronous programming with the Loops SDK:
 * <ul>
 *   <li>Basic async operations with CompletableFuture</li>
 *   <li>Parallel execution of independent operations</li>
 *   <li>Chaining dependent operations</li>
 *   <li>Error handling in async flows</li>
 *   <li>Implementing retry logic</li>
 *   <li>Batch operations with concurrent requests</li>
 *   <li>Timeouts and cancellation</li>
 *   <li>Combining multiple async results</li>
 * </ul>
 *
 * <p>Asynchronous operations are essential for building high-performance applications
 * that need to handle multiple API calls efficiently without blocking.
 *
 * <p><b>Prerequisites:</b>
 * <ul>
 *   <li>Set the LOOPS_API_KEY environment variable</li>
 *   <li>Understanding of Java CompletableFuture API</li>
 *   <li>Familiarity with async/await patterns</li>
 * </ul>
 *
 * <p><b>To run this example:</b>
 * <pre>
 * export LOOPS_API_KEY="your-api-key-here"
 * java com.telos.loops.examples.AsyncExample
 * </pre>
 */
public class AsyncExample {

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
      // Example 1: Basic Async Operation
      // ========================================
      System.out.println("=== Example 1: Basic Async Operation ===");

      ContactCreateRequest contactRequest = new ContactCreateRequest(
          "async.user@example.com",
          "Async",
          "User",
          true,
          null,
          "async_001",
          null,
          null
      );

      // Start async operation - returns immediately with CompletableFuture
      CompletableFuture<ContactResponse> futureResponse =
          client.contacts().createAsync(contactRequest);

      System.out.println("Contact creation started asynchronously...");

      // Do other work here while the request is in flight
      System.out.println("Doing other work while waiting...");

      // Wait for completion and get result
      ContactResponse response = futureResponse.get(); // Blocks until complete
      System.out.println("Contact created: " + response.success());

      // ========================================
      // Example 2: Parallel Independent Operations
      // ========================================
      System.out.println("\n=== Example 2: Parallel Independent Operations ===");

      Instant startTime = Instant.now();

      // Launch multiple independent operations in parallel
      CompletableFuture<ContactResponse> contact1Future = client.contacts().createAsync(
          new ContactCreateRequest("parallel1@example.com", "Parallel", "One", true, null, null, null, null)
      );

      CompletableFuture<ContactResponse> contact2Future = client.contacts().createAsync(
          new ContactCreateRequest("parallel2@example.com", "Parallel", "Two", true, null, null, null, null)
      );

      CompletableFuture<ContactResponse> contact3Future = client.contacts().createAsync(
          new ContactCreateRequest("parallel3@example.com", "Parallel", "Three", true, null, null, null, null)
      );

      // Wait for all to complete
      CompletableFuture<Void> allContacts = CompletableFuture.allOf(
          contact1Future, contact2Future, contact3Future
      );

      allContacts.get(); // Wait for all to finish

      Instant endTime = Instant.now();
      long elapsed = Duration.between(startTime, endTime).toMillis();

      System.out.println("Created 3 contacts in parallel");
      System.out.println("Total time: " + elapsed + "ms");
      System.out.println("Note: Parallel execution is much faster than sequential");

      // ========================================
      // Example 3: Chaining Dependent Operations
      // ========================================
      System.out.println("\n=== Example 3: Chaining Dependent Operations ===");

      // Create a contact, then send them an event, then send a transactional email
      // Each operation depends on the previous one
      String chainEmail = "chained@example.com";

      CompletableFuture<String> chainedFlow = client.contacts().createAsync(
              new ContactCreateRequest(chainEmail, "Chained", "User", true, null, null, null, null)
          )
          .thenCompose(createResp -> {
            System.out.println("Step 1: Contact created");
            // After contact created, send an event
            return client.events().sendAsync(
                new EventSendRequest(chainEmail, null, "user_onboarded", null, null, null)
            );
          })
          .thenCompose(eventResp -> {
            System.out.println("Step 2: Event sent");
            // After event sent, send welcome email
            Map<String, Object> emailData = new HashMap<>();
            emailData.put("user_name", "Chained User");

            return client.transactional().sendAsync(
                new TransactionalSendRequest(TEMPLATE_ID, chainEmail, false, emailData, null)
            );
          })
          .thenApply(transResp -> {
            System.out.println("Step 3: Transactional email sent");
            return "Complete onboarding flow finished successfully!";
          });

      String chainResult = chainedFlow.get();
      System.out.println(chainResult);

      // ========================================
      // Example 4: Error Handling in Async Flows
      // ========================================
      System.out.println("\n=== Example 4: Error Handling ===");

      // Attempt an operation that might fail
      CompletableFuture<ContactResponse> riskyOperation = client.contacts().createAsync(
              new ContactCreateRequest("error.test@example.com", null, null, true, null, null, null, null)
          )
          .exceptionally(throwable -> {
            // Handle errors gracefully
            System.err.println("Error occurred: " + throwable.getMessage());
            // Return a default value or rethrow
            return new ContactResponse(false, null, "Error occurred");
          });

      ContactResponse riskyResult = riskyOperation.get();
      System.out.println("Operation completed with result: " + riskyResult.success());

      // More sophisticated error handling with handle()
      CompletableFuture<String> advancedErrorHandling = client.contacts().createAsync(
              new ContactCreateRequest("advanced@example.com", null, null, true, null, null, null, null)
          )
          .handle((result, throwable) -> {
            if (throwable != null) {
              System.err.println("Error in async operation: " + throwable.getMessage());
              return "Operation failed: " + throwable.getMessage();
            } else {
              return "Operation succeeded with ID: " + result.id();
            }
          });

      String errorHandlingResult = advancedErrorHandling.get();
      System.out.println(errorHandlingResult);

      // ========================================
      // Example 5: Retry Logic
      // ========================================
      System.out.println("\n=== Example 5: Implementing Retry Logic ===");

      CompletableFuture<ContactResponse> retryableOperation = retryAsync(
          () -> client.contacts().createAsync(
              new ContactCreateRequest("retry@example.com", "Retry", "User", true, null, null, null, null)
          ),
          3,  // Max retry attempts
          1000 // Delay between retries (ms)
      );

      ContactResponse retryResult = retryableOperation.get();
      System.out.println("Retryable operation completed: " + retryResult.success());

      // ========================================
      // Example 6: Batch Operations
      // ========================================
      System.out.println("\n=== Example 6: Batch Operations ===");

      // Create multiple contacts efficiently
      List<String> emailsToBatch = List.of(
          "batch1@example.com",
          "batch2@example.com",
          "batch3@example.com",
          "batch4@example.com",
          "batch5@example.com"
      );

      Instant batchStart = Instant.now();

      List<CompletableFuture<ContactResponse>> batchFutures = emailsToBatch.stream()
          .map(email -> client.contacts().createAsync(
              new ContactCreateRequest(email, "Batch", "User", true, null, null, null, null)
          ))
          .collect(Collectors.toList());

      // Wait for all batch operations
      CompletableFuture<Void> allBatch = CompletableFuture.allOf(
          batchFutures.toArray(new CompletableFuture[0])
      );

      allBatch.get();

      Instant batchEnd = Instant.now();
      long batchElapsed = Duration.between(batchStart, batchEnd).toMillis();

      // Collect results
      List<ContactResponse> batchResults = batchFutures.stream()
          .map(CompletableFuture::join)
          .collect(Collectors.toList());

      long successCount = batchResults.stream()
          .filter(ContactResponse::success)
          .count();

      System.out.println("Batch operation completed:");
      System.out.println("  Total contacts: " + emailsToBatch.size());
      System.out.println("  Successful: " + successCount);
      System.out.println("  Time elapsed: " + batchElapsed + "ms");

      // ========================================
      // Example 7: Timeouts
      // ========================================
      System.out.println("\n=== Example 7: Handling Timeouts ===");

      try {
        // Set a timeout for the operation
        ContactResponse timeoutResult = client.contacts().createAsync(
                new ContactCreateRequest("timeout@example.com", null, null, true, null, null, null, null)
            )
            .orTimeout(5, TimeUnit.SECONDS) // Timeout after 5 seconds
            .get();

        System.out.println("Operation completed within timeout: " + timeoutResult.success());

      } catch (TimeoutException e) {
        System.err.println("Operation timed out!");
      } catch (ExecutionException | InterruptedException e) {
        System.err.println("Operation failed: " + e.getMessage());
      }

      // ========================================
      // Example 8: Combining Multiple Results
      // ========================================
      System.out.println("\n=== Example 8: Combining Multiple Async Results ===");

      // Fetch different types of data in parallel and combine
      CompletableFuture<List<Contact>> contactsFuture = client.contacts().findAsync(
          new ContactFindRequest("async.user@example.com", null)
      );

      CompletableFuture<List<MailingList>> listsFuture = client.mailingLists().listAsync();

      // Combine the results
      CompletableFuture<String> combinedFuture = contactsFuture.thenCombine(
          listsFuture,
          (contacts, lists) -> {
            // Combine data from both requests
            return String.format(
                "Found %d contact(s) and %d mailing list(s)",
                contacts.size(),
                lists.size()
            );
          }
      );

      String combinedResult = combinedFuture.get();
      System.out.println(combinedResult);

      // ========================================
      // Example 9: Complex Workflow
      // ========================================
      System.out.println("\n=== Example 9: Complex Async Workflow ===");

      // Simulate a complex workflow: create user, add to lists, send events, send email
      String workflowEmail = "workflow@example.com";

      CompletableFuture<String> complexWorkflow = CompletableFuture.supplyAsync(() -> {
            System.out.println("[Workflow] Starting complex workflow...");
            return workflowEmail;
          })
          .thenCompose(email -> {
            // Step 1: Create contact
            System.out.println("[Workflow] Step 1: Creating contact");
            return client.contacts().createAsync(
                new ContactCreateRequest(email, "Workflow", "User", true, null, null, null, null)
            );
          })
          .thenCompose(createResp -> {
            // Step 2: Send multiple events in parallel
            System.out.println("[Workflow] Step 2: Sending events");
            CompletableFuture<EventResponse> event1 = client.events().sendAsync(
                new EventSendRequest(workflowEmail, null, "signup_completed", null, null, null)
            );
            CompletableFuture<EventResponse> event2 = client.events().sendAsync(
                new EventSendRequest(workflowEmail, null, "profile_updated", null, null, null)
            );

            return CompletableFuture.allOf(event1, event2).thenApply(v -> createResp);
          })
          .thenCompose(createResp -> {
            // Step 3: Send welcome email
            System.out.println("[Workflow] Step 3: Sending welcome email");
            Map<String, Object> emailData = new HashMap<>();
            emailData.put("user_name", "Workflow User");

            return client.transactional().sendAsync(
                new TransactionalSendRequest(TEMPLATE_ID, workflowEmail, false, emailData, null)
            );
          })
          .thenApply(transResp -> {
            System.out.println("[Workflow] All steps completed successfully");
            return "Complex workflow finished!";
          })
          .exceptionally(throwable -> {
            System.err.println("[Workflow] Error: " + throwable.getMessage());
            return "Workflow failed: " + throwable.getMessage();
          });

      String workflowResult = complexWorkflow.get();
      System.out.println(workflowResult);

      System.out.println("\n=== Async Operations Example Complete ===");
      System.out.println("Key Takeaways:");
      System.out.println("- Use async methods for non-blocking operations");
      System.out.println("- Parallel execution improves performance significantly");
      System.out.println("- Chain operations with thenCompose() for dependent flows");
      System.out.println("- Always handle errors with exceptionally() or handle()");
      System.out.println("- Implement retries for resilient applications");
      System.out.println("- Use timeouts to prevent indefinite blocking");

    } catch (ExecutionException e) {
      // Unwrap the actual cause
      Throwable cause = e.getCause();
      if (cause instanceof LoopsApiException apiException) {
        System.err.println("\nAPI Error in async operation:");
        System.err.println("Status: " + apiException.statusCode());
        System.err.println("Message: " + apiException.getMessage());
      } else if (cause instanceof CompletionException) {
        System.err.println("\nCompletion error: " + cause.getCause().getMessage());
      } else {
        System.err.println("\nExecution error: " + cause.getMessage());
        cause.printStackTrace();
      }
      System.exit(1);

    } catch (InterruptedException e) {
      System.err.println("\nOperation interrupted: " + e.getMessage());
      Thread.currentThread().interrupt();
      System.exit(1);

    } catch (Exception e) {
      System.err.println("\nUnexpected error:");
      System.err.println("Type: " + e.getClass().getSimpleName());
      System.err.println("Message: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Retry a CompletableFuture operation with exponential backoff.
   *
   * @param operation The async operation to retry
   * @param maxRetries Maximum number of retry attempts
   * @param delayMs Initial delay between retries in milliseconds
   * @return CompletableFuture with the result
   */
  private static <T> CompletableFuture<T> retryAsync(
      java.util.function.Supplier<CompletableFuture<T>> operation,
      int maxRetries,
      long delayMs) {

    CompletableFuture<T> future = operation.get();

    for (int i = 0; i < maxRetries; i++) {
      final int attempt = i + 1;
      final long currentDelay = delayMs * (long) Math.pow(2, i); // Exponential backoff

      future = future.exceptionallyCompose(throwable -> {
        System.out.println("Retry attempt " + attempt + " after " + currentDelay + "ms delay");

        // Create a delayed retry
        CompletableFuture<Void> delay = new CompletableFuture<>();
        CompletableFuture.delayedExecutor(currentDelay, TimeUnit.MILLISECONDS)
            .execute(() -> delay.complete(null));

        return delay.thenCompose(v -> operation.get());
      });
    }

    return future;
  }
}
