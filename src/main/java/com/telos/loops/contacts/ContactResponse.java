package com.telos.loops.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response from creating, updating, or deleting a contact.
 *
 * <p>
 * This response confirms the operation and provides the contact's ID and a
 * status message.
 *
 * @param id      the unique identifier for the contact (for create/update
 *                operations)
 * @param message a status message describing the result (e.g., "Contact
 *                created", "Contact
 *                updated")
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactResponse(String id, String message) {
    /**
     * Creates a new builder for {@link ContactResponse}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder for {@link ContactResponse}. */
    public static final class Builder {
        private String id;
        private String message;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public ContactResponse build() {
            return new ContactResponse(id, message);
        }
    }
}
