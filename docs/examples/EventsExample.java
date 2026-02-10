package com.telos.loops.examples;

import com.telos.loops.LoopsClient;
import com.telos.loops.error.LoopsApiException;
import com.telos.loops.events.EventResponse;
import com.telos.loops.events.EventSendRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Events Example - Event Sending with Different Scenarios
 *
 * <p>This example demonstrates various event-sending patterns:
 * <ul>
 *   <li>Sending basic events without properties</li>
 *   <li>Sending events with custom properties for personalization</li>
 *   <li>User lifecycle events (signup, activation, churn)</li>
 *   <li>User engagement events (feature usage, content interaction)</li>
 *   <li>Transaction events (purchases, subscriptions)</li>
 *   <li>Using idempotency keys to prevent duplicate events</li>
 *   <li>Targeting events by email or userId</li>
 *   <li>Adding contacts to mailing lists via events</li>
 * </ul>
 *
 * <p>Events are the primary mechanism for triggering automated email campaigns in Loops.
 * Each event can carry custom properties that personalize the resulting emails.
 *
 * <p><b>Prerequisites:</b>
 * <ul>
 *   <li>Set the LOOPS_API_KEY environment variable</li>
 *   <li>Create event-triggered campaigns in your Loops account</li>
 *   <li>Have contacts in your audience (or events will create them)</li>
 * </ul>
 *
 * <p><b>To run this example:</b>
 * <pre>
 * export LOOPS_API_KEY="your-api-key-here"
 * java com.telos.loops.examples.EventsExample
 * </pre>
 */
public class EventsExample {

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
      // Example 1: Basic Event (No Properties)
      // ========================================
      System.out.println("=== Example 1: Sending a Basic Event ===");

      // The simplest event requires only an email/userId and event name
      // This triggers any campaigns configured for this event
      EventSendRequest basicEvent = new EventSendRequest(
          "user@example.com",  // email (required if userId not provided)
          null,                // userId (alternative to email)
          "welcome_sent",      // eventName (required)
          null,                // eventProperties (optional)
          null,                // mailingLists (optional)
          null                 // additionalProperties (optional)
      );

      EventResponse basicResponse = client.events().send(basicEvent);
      System.out.println("Basic event sent: " + basicResponse.success());

      // ========================================
      // Example 2: Event with Properties
      // ========================================
      System.out.println("\n=== Example 2: Event with Custom Properties ===");

      // Event properties personalize the triggered emails
      // These values can be referenced in your email templates using {{property_name}}
      Map<String, Object> eventProperties = new HashMap<>();
      eventProperties.put("user_name", "Sarah Connor");
      eventProperties.put("trial_days", 14);
      eventProperties.put("feature_enabled", true);
      eventProperties.put("signup_date", Instant.now().toString());

      EventSendRequest eventWithProps = new EventSendRequest(
          "sarah.connor@example.com",
          null,
          "trial_started",
          eventProperties,
          null,
          null
      );

      EventResponse propsResponse = client.events().send(eventWithProps);
      System.out.println("Event with properties sent: " + propsResponse.success());

      // ========================================
      // Example 3: User Lifecycle Events
      // ========================================
      System.out.println("\n=== Example 3: User Lifecycle Events ===");

      // Signup event - When a user creates an account
      Map<String, Object> signupProps = new HashMap<>();
      signupProps.put("signup_method", "google_oauth");
      signupProps.put("referral_source", "product_hunt");
      signupProps.put("account_type", "individual");

      EventSendRequest signupEvent = new EventSendRequest(
          "newuser@example.com",
          null,
          "user_signed_up",
          signupProps,
          null,
          null
      );

      EventResponse signupResponse = client.events().send(signupEvent);
      System.out.println("Signup event sent: " + signupResponse.success());

      // Activation event - When a user completes key setup steps
      Map<String, Object> activationProps = new HashMap<>();
      activationProps.put("steps_completed", 5);
      activationProps.put("time_to_activate_minutes", 15);
      activationProps.put("first_project_name", "My Awesome Project");

      EventSendRequest activationEvent = new EventSendRequest(
          null,
          "user_12345",        // Use userId instead of email
          "user_activated",
          activationProps,
          null,
          null
      );

      EventResponse activationResponse = client.events().send(activationEvent);
      System.out.println("Activation event sent: " + activationResponse.success());

      // Churn event - When a user cancels or becomes inactive
      Map<String, Object> churnProps = new HashMap<>();
      churnProps.put("cancellation_reason", "too_expensive");
      churnProps.put("days_active", 45);
      churnProps.put("feedback", "Great product, but out of budget");

      EventSendRequest churnEvent = new EventSendRequest(
          "churned@example.com",
          null,
          "user_churned",
          churnProps,
          null,
          null
      );

      EventResponse churnResponse = client.events().send(churnEvent);
      System.out.println("Churn event sent: " + churnResponse.success());

      // ========================================
      // Example 4: Engagement Events
      // ========================================
      System.out.println("\n=== Example 4: User Engagement Events ===");

      // Feature usage event
      Map<String, Object> featureProps = new HashMap<>();
      featureProps.put("feature_name", "advanced_analytics");
      featureProps.put("usage_count", 10);
      featureProps.put("user_tier", "premium");

      EventSendRequest featureEvent = new EventSendRequest(
          "active.user@example.com",
          null,
          "feature_used",
          featureProps,
          null,
          null
      );

      EventResponse featureResponse = client.events().send(featureEvent);
      System.out.println("Feature usage event sent: " + featureResponse.success());

      // Content interaction event
      Map<String, Object> contentProps = new HashMap<>();
      contentProps.put("content_type", "blog_post");
      contentProps.put("content_title", "10 Tips for Success");
      contentProps.put("reading_time_seconds", 180);
      contentProps.put("completed", true);

      EventSendRequest contentEvent = new EventSendRequest(
          "reader@example.com",
          null,
          "content_viewed",
          contentProps,
          null,
          null
      );

      EventResponse contentResponse = client.events().send(contentEvent);
      System.out.println("Content interaction event sent: " + contentResponse.success());

      // ========================================
      // Example 5: Transaction Events
      // ========================================
      System.out.println("\n=== Example 5: Transaction Events ===");

      // Purchase event
      Map<String, Object> purchaseProps = new HashMap<>();
      purchaseProps.put("product_name", "Pro Plan Annual");
      purchaseProps.put("amount", 299.99);
      purchaseProps.put("currency", "USD");
      purchaseProps.put("transaction_id", "txn_" + UUID.randomUUID().toString());
      purchaseProps.put("payment_method", "credit_card");

      EventSendRequest purchaseEvent = new EventSendRequest(
          "buyer@example.com",
          null,
          "purchase_completed",
          purchaseProps,
          null,
          null
      );

      EventResponse purchaseResponse = client.events().send(purchaseEvent);
      System.out.println("Purchase event sent: " + purchaseResponse.success());

      // Subscription renewal event
      Map<String, Object> renewalProps = new HashMap<>();
      renewalProps.put("plan_name", "Enterprise");
      renewalProps.put("renewal_date", "2025-02-10");
      renewalProps.put("amount", 999.00);
      renewalProps.put("billing_cycle", "annual");

      EventSendRequest renewalEvent = new EventSendRequest(
          "enterprise@example.com",
          null,
          "subscription_renewed",
          renewalProps,
          null,
          null
      );

      EventResponse renewalResponse = client.events().send(renewalEvent);
      System.out.println("Subscription renewal event sent: " + renewalResponse.success());

      // ========================================
      // Example 6: Idempotency
      // ========================================
      System.out.println("\n=== Example 6: Using Idempotency Keys ===");

      // Idempotency keys prevent duplicate events in case of retries
      // Use the same key for the same logical event
      String idempotencyKey = "event_" + UUID.randomUUID().toString();

      Map<String, Object> importantEventProps = new HashMap<>();
      importantEventProps.put("order_id", "ORD-12345");
      importantEventProps.put("total", 159.99);

      EventSendRequest idempotentEvent = new EventSendRequest(
          "customer@example.com",
          null,
          "order_confirmed",
          importantEventProps,
          null,
          null
      );

      // Send event with idempotency key
      EventResponse idempotentResponse1 = client.events().send(idempotentEvent, idempotencyKey);
      System.out.println("First send with idempotency key: " + idempotentResponse1.success());

      // Send the exact same event again with the same key
      // Loops will recognize this as a duplicate and not trigger another campaign
      EventResponse idempotentResponse2 = client.events().send(idempotentEvent, idempotencyKey);
      System.out.println("Second send (duplicate): " + idempotentResponse2.success());
      System.out.println("Note: Duplicate events are handled gracefully by the API");

      // ========================================
      // Example 7: Mailing List Management
      // ========================================
      System.out.println("\n=== Example 7: Managing Mailing Lists via Events ===");

      // You can update a contact's mailing list subscriptions when sending an event
      Map<String, Boolean> mailingLists = new HashMap<>();
      mailingLists.put("cm07ts3sx00a7l80oumin5cyi", true);   // Subscribe to newsletter
      mailingLists.put("cm07ts78i00a9l80ov2qjwvki", false);  // Unsubscribe from promotions

      Map<String, Object> eventPropsWithLists = new HashMap<>();
      eventPropsWithLists.put("preference_updated", true);

      EventSendRequest eventWithLists = new EventSendRequest(
          "preferences@example.com",
          null,
          "preferences_updated",
          eventPropsWithLists,
          mailingLists,  // Update mailing list subscriptions
          null
      );

      EventResponse listsResponse = client.events().send(eventWithLists);
      System.out.println("Event with mailing list updates sent: " + listsResponse.success());

      // ========================================
      // Example 8: Best Practices
      // ========================================
      System.out.println("\n=== Example 8: Event Best Practices ===");

      // 1. Use descriptive event names (past tense recommended)
      // 2. Include relevant context in properties
      // 3. Keep property names consistent across events
      // 4. Use appropriate data types (numbers, booleans, strings)
      // 5. Include timestamps for time-sensitive data

      Map<String, Object> bestPracticeProps = new HashMap<>();
      bestPracticeProps.put("timestamp", Instant.now().toString());
      bestPracticeProps.put("user_name", "John Doe");
      bestPracticeProps.put("action_count", 5);
      bestPracticeProps.put("success", true);
      bestPracticeProps.put("category", "engagement");

      EventSendRequest bestPracticeEvent = new EventSendRequest(
          "best.practice@example.com",
          "user_bp_123",  // Always good to include both email and userId when available
          "milestone_achieved",
          bestPracticeProps,
          null,
          null
      );

      // Generate a meaningful idempotency key based on the event's unique characteristics
      String meaningfulKey = "milestone_user_bp_123_" + Instant.now().getEpochSecond() / 3600;

      EventResponse bestPracticeResponse = client.events().send(bestPracticeEvent, meaningfulKey);
      System.out.println("Best practice event sent: " + bestPracticeResponse.success());

      System.out.println("\n=== Events Example Complete ===");
      System.out.println("Pro Tips:");
      System.out.println("- Events trigger automated campaigns configured in Loops");
      System.out.println("- Event properties personalize email content");
      System.out.println("- Use idempotency keys for critical events to prevent duplicates");
      System.out.println("- Track both email and userId for better user identification");

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
