package com.telos.loops.examples;

import com.telos.loops.LoopsClient;
import com.telos.loops.apikey.ApiKeyTestResponse;
import com.telos.loops.contacts.ContactCreateRequest;
import com.telos.loops.contacts.ContactResponse;
import com.telos.loops.error.LoopsApiException;
import com.telos.loops.events.EventResponse;
import com.telos.loops.events.EventSendRequest;
import com.telos.loops.transactional.TransactionalResponse;
import com.telos.loops.transactional.TransactionalSendRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * QuickStart Example - Basic SDK Usage
 *
 * <p>This example demonstrates the essential features of the Loops Java SDK:
 * <ul>
 *   <li>Initializing the SDK client with an API key</li>
 *   <li>Testing API key validity</li>
 *   <li>Creating a new contact in your audience</li>
 *   <li>Sending an event to trigger email campaigns</li>
 *   <li>Sending a transactional email</li>
 *   <li>Proper error handling</li>
 * </ul>
 *
 * <p>This is the perfect starting point for new users to understand the SDK's basic operations.
 *
 * <p><b>Prerequisites:</b>
 * <ul>
 *   <li>Set the LOOPS_API_KEY environment variable with your Loops API key</li>
 *   <li>Have a transactional email template created in your Loops account</li>
 * </ul>
 *
 * <p><b>To run this example:</b>
 * <pre>
 * export LOOPS_API_KEY="your-api-key-here"
 * java com.telos.loops.examples.QuickStart
 * </pre>
 */
public class QuickStart {

  public static void main(String[] args) {
    // Step 1: Retrieve API key from environment variable
    String apiKey = System.getenv("LOOPS_API_KEY");
    if (apiKey == null || apiKey.isBlank()) {
      System.err.println("Error: LOOPS_API_KEY environment variable is not set");
      System.err.println("Please set it with: export LOOPS_API_KEY=\"your-api-key\"");
      System.exit(1);
    }

    try {
      // Step 2: Initialize the Loops client
      // The builder pattern provides a clean way to construct the client
      // The API key is required; baseUrl is optional (defaults to production)
      System.out.println("Initializing Loops SDK client...");
      LoopsClient client = LoopsClient.builder()
          .apiKey(apiKey)
          .build();

      // Step 3: Test API key validity
      // This is a good first step to ensure your credentials are correct
      // before attempting other operations
      System.out.println("\n=== Testing API Key ===");
      ApiKeyTestResponse keyTest = client.apiKey().test();
      System.out.println("API Key is valid: " + keyTest.success());
      System.out.println("Team Name: " + keyTest.teamName());

      // Step 4: Create a new contact
      // Contacts are the foundation of your audience in Loops
      // Email is the only required field
      System.out.println("\n=== Creating Contact ===");
      ContactCreateRequest contactRequest = new ContactCreateRequest(
          "john.doe@example.com",     // email (required)
          "John",                      // firstName (optional)
          "Doe",                       // lastName (optional)
          true,                        // subscribed (default: false)
          "premium",                   // userGroup (optional)
          "user_123",                  // userId (optional)
          null,                        // mailingLists (optional)
          null                         // additionalProperties (optional)
      );

      ContactResponse contactResponse = client.contacts().create(contactRequest);
      System.out.println("Contact created successfully!");
      System.out.println("Success: " + contactResponse.success());
      System.out.println("Contact ID: " + contactResponse.id());

      // Step 5: Send an event
      // Events trigger email campaigns in Loops
      // You can include custom properties to personalize emails
      System.out.println("\n=== Sending Event ===");
      Map<String, Object> eventProperties = new HashMap<>();
      eventProperties.put("product_name", "Premium Plan");
      eventProperties.put("trial_days", 14);

      EventSendRequest eventRequest = new EventSendRequest(
          "john.doe@example.com",      // email (one of email or userId required)
          null,                        // userId (alternative to email)
          "trial_started",             // eventName (required)
          eventProperties,             // eventProperties (optional)
          null,                        // mailingLists (optional)
          null                         // additionalProperties (optional)
      );

      EventResponse eventResponse = client.events().send(eventRequest);
      System.out.println("Event sent successfully!");
      System.out.println("Success: " + eventResponse.success());

      // Step 6: Send a transactional email
      // Transactional emails are one-time messages (receipts, notifications, etc.)
      // They require a template ID from your Loops account
      System.out.println("\n=== Sending Transactional Email ===");

      // Data variables are merged into your email template
      Map<String, Object> dataVariables = new HashMap<>();
      dataVariables.put("name", "John Doe");
      dataVariables.put("welcome_message", "Welcome to our platform!");

      TransactionalSendRequest transactionalRequest = new TransactionalSendRequest(
          "cly8kzmsp000gl50oyf5qaj0h", // transactionalId (get from Loops dashboard)
          "john.doe@example.com",       // email (required)
          true,                         // addToAudience (optional, default: false)
          dataVariables,                // dataVariables (optional)
          null                          // attachments (optional)
      );

      TransactionalResponse transactionalResponse = client.transactional().send(transactionalRequest);
      System.out.println("Transactional email sent successfully!");
      System.out.println("Success: " + transactionalResponse.success());

      System.out.println("\n=== QuickStart Example Complete ===");
      System.out.println("All operations completed successfully!");

    } catch (LoopsApiException e) {
      // LoopsApiException is thrown for API-related errors (4xx, 5xx responses)
      System.err.println("\nAPI Error occurred:");
      System.err.println("Status Code: " + e.statusCode());
      System.err.println("Error Message: " + e.getMessage());
      if (e.error() != null) {
        System.err.println("Error Details: " + e.error());
      }
      System.exit(1);

    } catch (Exception e) {
      // Catch any other unexpected errors
      System.err.println("\nUnexpected error occurred:");
      System.err.println("Error Type: " + e.getClass().getSimpleName());
      System.err.println("Message: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }
}
