package com.telos.loops;

import com.telos.loops.apikey.ApiKeyClient;
import com.telos.loops.contacts.ContactsClient;
import com.telos.loops.events.EventsClient;
import com.telos.loops.internal.CoreSender;
import com.telos.loops.ips.DedicatedIpsClient;
import com.telos.loops.lists.MailingListsClient;
import com.telos.loops.properties.ContactPropertiesClient;
import com.telos.loops.transactional.TransactionalClient;
import com.telos.loops.transport.OkHttpTransport;
import com.telos.loops.transport.Transport;

/**
 * Main client for interacting with the Loops API.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * LoopsClient client = LoopsClient.builder()
 *         .apiKey("your-api-key")
 *         .build();
 *
 * // Test API key
 * ApiKeyTestResponse response = client.apiKey().test();
 *
 * // Create a contact
 * var contact = new ContactUpdateRequest("user@example.com", null, null, null, null, null, null);
 * client.contacts().create(contact);
 *
 * // Send an event
 * var event = new EventSendRequest("eventName", "user@example.com", null, null, null);
 * client.events().send(event);
 * }</pre>
 */
public class LoopsClient {

  private final ContactsClient contactsClient;
  private final EventsClient eventsClient;
  private final ApiKeyClient apiKeyClient;
  private final DedicatedIpsClient dedicatedIpsClient;
  private final MailingListsClient mailingListsClient;
  private final ContactPropertiesClient contactPropertiesClient;
  private final TransactionalClient transactionalClient;

  private LoopsClient(CoreSender coreSender) {

    this.contactsClient = new ContactsClient(coreSender);
    this.eventsClient = new EventsClient(coreSender);
    this.apiKeyClient = new ApiKeyClient(coreSender);
    this.dedicatedIpsClient = new DedicatedIpsClient(coreSender);
    this.mailingListsClient = new MailingListsClient(coreSender);
    this.contactPropertiesClient = new ContactPropertiesClient(coreSender);
    this.transactionalClient = new TransactionalClient(coreSender);
  }

  /**
   * Creates a new builder for constructing a LoopsClient.
   *
   * @return a new Builder instance
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Returns the Contacts API client for managing contacts in your audience.
   *
   * @return ContactsClient instance
   */
  public ContactsClient contacts() {
    return contactsClient;
  }

  /**
   * Returns the Events API client for triggering email sending with events.
   *
   * @return EventsClient instance
   */
  public EventsClient events() {
    return eventsClient;
  }

  /**
   * Returns the API Key client for testing API key validity.
   *
   * @return ApiKeyClient instance
   */
  public ApiKeyClient apiKey() {
    return apiKeyClient;
  }

  /**
   * Returns the Dedicated IPs client for viewing dedicated sending IP addresses.
   *
   * @return DedicatedIpsClient instance
   */
  public DedicatedIpsClient dedicatedIps() {
    return dedicatedIpsClient;
  }

  /**
   * Returns the Mailing Lists client for viewing mailing lists.
   *
   * @return MailingListsClient instance
   */
  public MailingListsClient mailingLists() {
    return mailingListsClient;
  }

  /**
   * Returns the Contact Properties client for managing contact properties.
   *
   * @return ContactPropertiesClient instance
   */
  public ContactPropertiesClient contactProperties() {
    return contactPropertiesClient;
  }

  /**
   * Returns the Transactional client for sending and viewing transactional emails.
   *
   * @return TransactionalClient instance
   */
  public TransactionalClient transactional() {
    return transactionalClient;
  }

  /** Builder for constructing a LoopsClient instance. */
  public static class Builder {
    private String apiKey;
    private String baseUrl = "https://app.loops.so/api/v1";

    private Builder() {}

    /**
     * Sets the API key for authentication.
     *
     * @param apiKey the Loops API key
     * @return this Builder instance
     */
    public Builder apiKey(String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    /**
     * Sets a custom base URL for the Loops API (optional).
     *
     * @param baseUrl the base URL
     * @return this Builder instance
     */
    public Builder baseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }

    /**
     * Builds and returns a new LoopsClient instance.
     *
     * @return a new LoopsClient
     * @throws IllegalArgumentException if apiKey is not set
     */
    public LoopsClient build() {
      if (apiKey == null || apiKey.isBlank()) {
        throw new IllegalArgumentException("API key is required");
      }
      Transport transport = new OkHttpTransport();
      CoreSender coreSender = new CoreSender(transport, baseUrl, apiKey);
      return new LoopsClient(coreSender);
    }
  }
}
