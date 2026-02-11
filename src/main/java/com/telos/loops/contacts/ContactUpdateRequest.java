package com.telos.loops.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

/**
 * Request to update an existing contact in Loops.
 *
 * <p>
 * You must provide either an email or userId to identify the contact. Only the
 * fields you
 * provide will be updated; omitted fields remain unchanged. Custom properties
 * can be updated via
 * the additionalProperties map.
 *
 * <p>
 * Example usage:
 *
 * <pre>{@code
 * ContactUpdateRequest request = new ContactUpdateRequest(
 *         "user@example.com", // email (identifier)
 *         "Jane", // firstName (update)
 *         null, // lastName (no change)
 *         null, // subscribed (no change)
 *         "enterprise", // userGroup (update)
 *         null, // userId
 *         null, // mailingLists (no change)
 *         Map.of("planName", "Enterprise") // additionalProperties (update)
 * );
 * }</pre>
 *
 * @param email                the email address of the contact to update (used
 *                             as identifier or to update email)
 * @param firstName            the contact's first name (null to leave
 *                             unchanged)
 * @param lastName             the contact's last name (null to leave unchanged)
 * @param subscribed           whether the contact should be subscribed (null to
 *                             leave unchanged)
 * @param userGroup            the user group to assign (null to leave
 *                             unchanged)
 * @param userId               your application's unique identifier (used as
 *                             identifier or to update userId)
 * @param mailingLists         map of mailing list IDs to subscription status
 *                             (null to leave unchanged)
 * @param additionalProperties custom contact properties to update (null to
 *                             leave unchanged)
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactUpdateRequest(
        String email,
        String firstName,
        String lastName,
        Boolean subscribed,
        String userGroup,
        String userId,
        Map<String, Boolean> mailingLists,
        Map<String, Object> additionalProperties) {

    public ContactUpdateRequest {
        // Defensive copy of maps to ensure immutability, but allow nulls as they mean
        // "no change"
        if (mailingLists != null) {
            mailingLists = Map.copyOf(mailingLists);
        }
        if (additionalProperties != null) {
            additionalProperties = Map.copyOf(additionalProperties);
        }
    }

    /**
     * Creates a new builder for {@link ContactUpdateRequest}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder for {@link ContactUpdateRequest}. */
    public static final class Builder {
        private String email;
        private String firstName;
        private String lastName;
        private Boolean subscribed;
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

        public Builder subscribed(Boolean subscribed) {
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

        public ContactUpdateRequest build() {
            return new ContactUpdateRequest(
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
