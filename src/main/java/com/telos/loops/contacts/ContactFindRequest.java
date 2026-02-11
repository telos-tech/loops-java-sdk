package com.telos.loops.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Request to find a contact in Loops.
 *
 * <p>
 * You must provide either an email address or a userId to identify the contact.
 * Providing both
 * is allowed but not required.
 *
 * @param email  the email address of the contact to find
 * @param userId your application's unique identifier for the contact to find
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactFindRequest(String email, String userId) {
    /**
     * Creates a new builder for {@link ContactFindRequest}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder for {@link ContactFindRequest}. */
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

        public ContactFindRequest build() {
            return new ContactFindRequest(email, userId);
        }
    }
}
