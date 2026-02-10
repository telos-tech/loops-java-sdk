package com.telos.loops.transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response from sending a transactional email.
 *
 * <p>
 * This response confirms whether the email was successfully queued for
 * delivery.
 *
 * @param success whether the transactional email was successfully sent
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record TransactionalResponse(boolean success) {
    /**
     * Creates a new builder for {@link TransactionalResponse}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder for {@link TransactionalResponse}. */
    public static final class Builder {
        private boolean success;

        private Builder() {
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public TransactionalResponse build() {
            return new TransactionalResponse(success);
        }
    }
}
