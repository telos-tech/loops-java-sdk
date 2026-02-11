package com.telos.loops.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response from creating a custom contact property.
 *
 * <p>
 * This response confirms whether the property was successfully created.
 *
 * @param success whether the contact property was successfully created
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactPropertyResponse(boolean success) {
    /**
     * Creates a new builder for {@link ContactPropertyResponse}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder for {@link ContactPropertyResponse}. */
    public static final class Builder {
        private boolean success;

        private Builder() {
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public ContactPropertyResponse build() {
            return new ContactPropertyResponse(success);
        }
    }
}
