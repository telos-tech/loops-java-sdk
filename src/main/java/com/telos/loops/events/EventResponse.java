package com.telos.loops.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response from sending an event to Loops.
 *
 * <p>
 * This response confirms whether the event was successfully recorded.
 *
 * @param success whether the event was successfully sent and recorded
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record EventResponse(boolean success) {
    /**
     * Creates a new builder for {@link EventResponse}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder for {@link EventResponse}. */
    public static final class Builder {
        private boolean success;

        private Builder() {
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public EventResponse build() {
            return new EventResponse(success);
        }
    }
}
