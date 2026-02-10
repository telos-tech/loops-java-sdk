package com.telos.loops.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Request to delete a contact from Loops.
 *
 * <p>
 * You must provide either an email address or a userId to identify the contact
 * to delete.
 * Providing both is allowed but not required.
 *
 * @param email  the email address of the contact to delete
 * @param userId your application's unique identifier for the contact to delete
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactDeleteRequest(String email, String userId) {
    /**
     * Creates a new builder for {@link ContactDeleteRequest}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder for {@link ContactDeleteRequest}. */
    public static final class Builder {
        private String email;
        private String userId;

        private Builder() {
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public ContactDeleteRequest build() {
            return new ContactDeleteRequest(email, userId);
        }
    }
}
