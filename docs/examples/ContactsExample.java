package com.telos.loops.examples;

import com.telos.loops.LoopsClient;
import com.telos.loops.contacts.Contact;
import com.telos.loops.contacts.ContactCreateRequest;
import com.telos.loops.contacts.ContactDeleteRequest;
import com.telos.loops.contacts.ContactFindRequest;
import com.telos.loops.contacts.ContactResponse;
import com.telos.loops.contacts.ContactUpdateRequest;
import com.telos.loops.error.LoopsApiException;
import com.telos.loops.lists.MailingList;
import com.telos.loops.properties.ContactPropertiesClient;
import com.telos.loops.properties.ContactProperty;
import com.telos.loops.properties.ContactPropertyCreateRequest;
import com.telos.loops.properties.ContactPropertyResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contacts Example - Complete Contact Management
 *
 * <p>This example demonstrates comprehensive contact management operations:
 * <ul>
 *   <li>Creating contacts with various attributes</li>
 *   <li>Updating contact information</li>
 *   <li>Finding contacts by email or user ID</li>
 *   <li>Managing custom contact properties</li>
 *   <li>Adding contacts to mailing lists</li>
 *   <li>Deleting contacts</li>
 *   <li>Working with custom fields and metadata</li>
 * </ul>
 *
 * <p>Contacts are the core of your audience in Loops. This example shows you how to
 * perform all CRUD operations and manage advanced contact attributes.
 *
 * <p><b>Prerequisites:</b>
 * <ul>
 *   <li>Set the LOOPS_API_KEY environment variable</li>
 *   <li>Have at least one mailing list created in Loops (optional)</li>
 * </ul>
 *
 * <p><b>To run this example:</b>
 * <pre>
 * export LOOPS_API_KEY="your-api-key-here"
 * java com.telos.loops.examples.ContactsExample
 * </pre>
 */
public class ContactsExample {

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
      // Example 1: Create a Basic Contact
      // ========================================
      System.out.println("=== Example 1: Creating a Basic Contact ===");

      // The simplest contact creation requires only an email
      ContactCreateRequest basicContact = new ContactCreateRequest(
          "jane.smith@example.com",
          null,    // firstName
          null,    // lastName
          false,   // subscribed
          null,    // userGroup
          null,    // userId
          null,    // mailingLists
          null     // additionalProperties
      );

      ContactResponse createResponse = client.contacts().create(basicContact);
      System.out.println("Basic contact created: " + createResponse.success());
      System.out.println("Contact ID: " + createResponse.id());

      // ========================================
      // Example 2: Create a Contact with All Attributes
      // ========================================
      System.out.println("\n=== Example 2: Creating a Full Contact ===");

      // Prepare mailing list subscriptions
      // Map keys are mailing list IDs, values indicate subscription status
      Map<String, Boolean> mailingLists = new HashMap<>();
      mailingLists.put("cm07ts3sx00a7l80oumin5cyi", true);   // Subscribe to newsletter
      mailingLists.put("cm07ts78i00a9l80ov2qjwvki", false);  // Not subscribed to promotions

      // Custom properties allow you to store additional contact data
      // These fields must be created in your Loops account first
      Map<String, Object> customProperties = new HashMap<>();
      customProperties.put("company", "Acme Corp");
      customProperties.put("plan_type", "enterprise");
      customProperties.put("account_value", 50000.00);
      customProperties.put("signup_date", "2024-01-15");

      ContactCreateRequest fullContact = new ContactCreateRequest(
          "alex.johnson@example.com",
          "Alex",
          "Johnson",
          true,                    // Subscribed to emails
          "enterprise",            // User group for segmentation
          "user_456",             // Your internal user ID
          mailingLists,
          customProperties
      );

      ContactResponse fullCreateResponse = client.contacts().create(fullContact);
      System.out.println("Full contact created: " + fullCreateResponse.success());
      System.out.println("Contact ID: " + fullCreateResponse.id());

      // ========================================
      // Example 3: Update a Contact
      // ========================================
      System.out.println("\n=== Example 3: Updating a Contact ===");

      // Update operations use email or userId to identify the contact
      // All fields are optional - only include what you want to change
      Map<String, Object> updatedProperties = new HashMap<>();
      updatedProperties.put("plan_type", "premium");  // Upgrade plan
      updatedProperties.put("last_login", "2024-02-10");

      ContactUpdateRequest updateRequest = new ContactUpdateRequest(
          "alex.johnson@example.com",  // Contact to update
          "Alexander",                  // Update first name
          null,                        // Keep last name unchanged
          null,                        // Keep subscription status unchanged
          "premium",                   // Update user group
          null,                        // Keep userId unchanged
          null,                        // Keep mailing lists unchanged
          updatedProperties
      );

      ContactResponse updateResponse = client.contacts().update(updateRequest);
      System.out.println("Contact updated: " + updateResponse.success());

      // You can also update by userId instead of email
      ContactUpdateRequest updateByUserId = new ContactUpdateRequest(
          null,        // email not provided
          null,
          null,
          true,        // Change subscription status
          null,
          "user_456",  // Identify by userId
          null,
          null
      );

      ContactResponse userIdUpdateResponse = client.contacts().update(updateByUserId);
      System.out.println("Contact updated by userId: " + userIdUpdateResponse.success());

      // ========================================
      // Example 4: Find Contacts
      // ========================================
      System.out.println("\n=== Example 4: Finding Contacts ===");

      // Find by email
      ContactFindRequest findByEmail = new ContactFindRequest(
          "alex.johnson@example.com",
          null  // userId
      );

      List<Contact> foundByEmail = client.contacts().find(findByEmail);
      if (!foundByEmail.isEmpty()) {
        Contact contact = foundByEmail.get(0);
        System.out.println("Found contact by email:");
        System.out.println("  ID: " + contact.id());
        System.out.println("  Email: " + contact.email());
        System.out.println("  Name: " + contact.firstName() + " " + contact.lastName());
        System.out.println("  Subscribed: " + contact.subscribed());
        System.out.println("  User Group: " + contact.userGroup());
        System.out.println("  Source: " + contact.source());

        // Mailing list memberships
        if (contact.mailingLists() != null && !contact.mailingLists().isEmpty()) {
          System.out.println("  Mailing Lists:");
          contact.mailingLists().forEach((listId, subscribed) ->
              System.out.println("    - " + listId + ": " + subscribed)
          );
        }
      } else {
        System.out.println("No contact found with that email");
      }

      // Find by userId
      ContactFindRequest findByUserId = new ContactFindRequest(
          null,
          "user_456"
      );

      List<Contact> foundByUserId = client.contacts().find(findByUserId);
      System.out.println("\nFound " + foundByUserId.size() + " contact(s) by userId");

      // ========================================
      // Example 5: Working with Mailing Lists
      // ========================================
      System.out.println("\n=== Example 5: Managing Mailing Lists ===");

      // List all available mailing lists
      List<MailingList> allLists = client.mailingLists().list();
      System.out.println("Available mailing lists:");
      for (MailingList list : allLists) {
        System.out.println("  - " + list.name() + " (ID: " + list.id() + ")");
      }

      // Subscribe a contact to specific mailing lists
      if (!allLists.isEmpty()) {
        Map<String, Boolean> listSubscriptions = new HashMap<>();
        // Subscribe to the first list
        listSubscriptions.put(allLists.get(0).id(), true);

        ContactUpdateRequest subscribeToLists = new ContactUpdateRequest(
            "jane.smith@example.com",
            null,
            null,
            null,
            null,
            null,
            listSubscriptions,  // Update list memberships
            null
        );

        ContactResponse listUpdateResponse = client.contacts().update(subscribeToLists);
        System.out.println("\nUpdated mailing list subscriptions: " + listUpdateResponse.success());
      }

      // ========================================
      // Example 6: Custom Contact Properties
      // ========================================
      System.out.println("\n=== Example 6: Managing Custom Properties ===");

      ContactPropertiesClient propertiesClient = client.contactProperties();

      // List existing custom properties
      List<ContactProperty> existingProperties = propertiesClient.list();
      System.out.println("Existing custom properties: " + existingProperties.size());
      for (ContactProperty prop : existingProperties) {
        System.out.println("  - " + prop.label() + " (" + prop.key() + ") - Type: " + prop.type());
      }

      // Create a new custom property
      // This property can then be used when creating/updating contacts
      ContactPropertyCreateRequest propertyRequest = new ContactPropertyCreateRequest(
          "lifetime_value",           // key (snake_case recommended)
          "Lifetime Value",            // label (display name)
          "number"                     // type: string, number, boolean, or date
      );

      try {
        ContactPropertyResponse propertyResponse = propertiesClient.create(propertyRequest);
        System.out.println("\nCreated custom property: " + propertyResponse.success());
      } catch (LoopsApiException e) {
        // Property might already exist
        System.out.println("\nProperty may already exist: " + e.getMessage());
      }

      // Now use the custom property on a contact
      Map<String, Object> propertiesWithCustomField = new HashMap<>();
      propertiesWithCustomField.put("lifetime_value", 15000.50);
      propertiesWithCustomField.put("last_purchase_date", "2024-02-08");

      ContactUpdateRequest updateWithCustom = new ContactUpdateRequest(
          "alex.johnson@example.com",
          null,
          null,
          null,
          null,
          null,
          null,
          propertiesWithCustomField
      );

      ContactResponse customUpdateResponse = client.contacts().update(updateWithCustom);
      System.out.println("Updated contact with custom properties: " + customUpdateResponse.success());

      // ========================================
      // Example 7: Delete a Contact
      // ========================================
      System.out.println("\n=== Example 7: Deleting a Contact ===");

      // Delete by email
      ContactDeleteRequest deleteRequest = new ContactDeleteRequest(
          "jane.smith@example.com",
          null  // userId
      );

      ContactResponse deleteResponse = client.contacts().delete(deleteRequest);
      System.out.println("Contact deleted: " + deleteResponse.success());

      // You can also delete by userId
      ContactDeleteRequest deleteByUserId = new ContactDeleteRequest(
          null,
          "user_789"  // Delete using internal userId
      );

      try {
        ContactResponse userIdDeleteResponse = client.contacts().delete(deleteByUserId);
        System.out.println("Contact deleted by userId: " + userIdDeleteResponse.success());
      } catch (LoopsApiException e) {
        System.out.println("Contact not found for deletion: " + e.getMessage());
      }

      System.out.println("\n=== Contacts Example Complete ===");

    } catch (LoopsApiException e) {
      System.err.println("\nAPI Error:");
      System.err.println("Status: " + e.statusCode());
      System.err.println("Message: " + e.getMessage());
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
