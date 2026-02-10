package com.telos.loops.apikey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nonnull;

/**
 * Response from testing an API key.
 *
 * <p>
 * This response indicates whether the API key is valid and provides the
 * associated team name.
 *
 * @param success  whether the API key is valid and authenticated successfully
 * @param teamName the name of the team associated with this API key
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ApiKeyTestResponse(@Nonnull Boolean success, @Nonnull String teamName) {
    /**
     * Creates a new builder for {@link ApiKeyTestResponse}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder for {@link ApiKeyTestResponse}. */
    public static final class Builder {
        private Boolean success;
        private String teamName;

        private Builder() {
        }

        public Builder success(Boolean success) {
            this.success = success;
            return this;
        }

        public Builder teamName(String teamName) {
            this.teamName = teamName;
            return this;
        }

        public ApiKeyTestResponse build() {
            return new ApiKeyTestResponse(success, teamName);
        }
    }
}
