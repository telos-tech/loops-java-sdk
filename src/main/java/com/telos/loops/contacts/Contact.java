package com.telos.loops.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

/**
 * Represents a contact in Loops.
 *
 * <p>
 * A contact contains profile information, subscription status, and mailing list
 * memberships.
 * This is returned when finding or listing contacts.
 *
 * @param id           the unique identifier for this contact
 * @param email        the contact's email address
 * @param firstName    the contact's first name
 * @param lastName     the contact's last name
 * @param source       how this contact was added (e.g., "API", "Import")
 * @param subscribed   whether the contact is subscribed to receive emails
 * @param userGroup    the user group this contact belongs to
 * @param userId       your application's unique identifier for this contact
 * @param mailingLists map of mailing list IDs to subscription status (true if
 *                     subscribed)
 * @param optInStatus  the contact's opt-in status (e.g., "single", "double",
 *                     "none")
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record Contact(
        String id,
        String email,
        String firstName,
        String lastName,
        String source,
        Boolean subscribed,
        String userGroup,
        String userId,
        Map<String, Boolean> mailingLists,
        String optInStatus) {

    public Contact {
        mailingLists = mailingLists == null ? java.util.Collections.emptyMap() : Map.copyOf(mailingLists);
    }

    /**
     * Creates a new builder for {@link Contact}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder for {@link Contact}. */
    public static final class Builder {
        private String id;
        private String email;
        private String firstName;
        private String lastName;
        private String source;
        private Boolean subscribed;
        private String userGroup;
        private String userId;
        private java.util.Map<String, Boolean> mailingLists;
        private String optInStatus;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
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

        public Builder source(String source) {
            this.source = source;
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

        public Builder optInStatus(String optInStatus) {
            this.optInStatus = optInStatus;
            return this;
        }

        public Contact build() {
            return new Contact(
                    id,
                    email,
                    firstName,
                    lastName,
                    source,
                    subscribed,
                    userGroup,
                    userId,
                    mailingLists,
                    optInStatus);
        }
    }
}
