package com.telos.loops.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nonnull;
import java.util.Map;

/**
 * Request to create a new contact in Loops.
 *
 * <p>
 * At minimum, you must provide an email address. All other fields are optional.
 * Custom
 * properties can be included via the additionalProperties map.
 *
 * <p>
 * Example usage:
 *
 * <pre>{@code
 * ContactCreateRequest request = new ContactCreateRequest(
 *         "user@example.com", // email (required)
 *         "John", // firstName
 *         "Doe", // lastName
 *         true, // subscribed
 *         "premium", // userGroup
 *         "user123", // userId
 *         Map.of("newsletter", true), // mailingLists
 *         Map.of("planName", "Pro") // additionalProperties
 * );
 * }</pre>
 *
 * @param email                the contact's email address (required)
 * @param firstName            the contact's first name
 * @param lastName             the contact's last name
 * @param subscribed           whether the contact should be subscribed
 *                             (defaults to true)
 * @param userGroup            the user group to assign this contact to
 * @param userId               your application's unique identifier for this
 *                             contact
 * @param mailingLists         map of mailing list IDs to subscription status
 *                             (true to subscribe)
 * @param additionalProperties custom contact properties (must match property
 *                             names defined in
 *                             Loops)
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactCreateRequest(
        @Nonnull String email,
        String firstName,
        String lastName,
        boolean subscribed,
        String userGroup,
        String userId,
        Map<String, Boolean> mailingLists,
        Map<String, Object> additionalProperties) {

    public ContactCreateRequest {
        // Defensive copy of maps to ensure immutability
        mailingLists = mailingLists == null ? java.util.Collections.emptyMap() : Map.copyOf(mailingLists);
        additionalProperties = additionalProperties == null
                ? java.util.Collections.emptyMap()
                : Map.copyOf(additionalProperties);
    }

    /**
     * Creates a new builder for {@link ContactCreateRequest}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder for {@link ContactCreateRequest}. */
    public static final class Builder {
        private String email;
        private String firstName;
        private String lastName;
        private boolean subscribed = true; // Default to true
        private String userGroup;
        private String userId;
        private java.util.Map<String, Boolean> mailingLists;
        private java.util.Map<String, Object> additionalProperties;

        private Builder() {
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder subscribed(boolean subscribed) {
            this.subscribed = subscribed;
            return this;
        }

        public Builder userGroup(String userGroup) {
            this.userGroup = userGroup;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder mailingLists(Map<String, Boolean> mailingLists) {
            this.mailingLists = mailingLists;
            return this;
        }

        public Builder putMailingList(String listId, boolean subscribed) {
            if (this.mailingLists == null) {
                this.mailingLists = new java.util.HashMap<>();
            }
            this.mailingLists.put(listId, subscribed);
            return this;
        }

        public Builder additionalProperties(Map<String, Object> additionalProperties) {
            this.additionalProperties = additionalProperties;
            return this;
        }

        public Builder putAdditionalProperty(String key, Object value) {
            if (this.additionalProperties == null) {
                this.additionalProperties = new java.util.HashMap<>();
            }
            this.additionalProperties.put(key, value);
            return this;
        }

        public ContactCreateRequest build() {
            return new ContactCreateRequest(
                    email,
                    firstName,
                    lastName,
                    subscribed,
                    userGroup,
                    userId,
                    mailingLists,
                    additionalProperties);
        }
    }
}
