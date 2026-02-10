package com.telos.loops.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a custom contact property definition in Loops.
 *
 * <p>
 * Contact properties allow you to store additional data fields on contacts
 * beyond the standard
 * fields (email, firstName, lastName, etc.). Properties must be defined before
 * they can be used.
 *
 * @param key   the property's key in camelCase format (e.g., "planName")
 * @param label the human-readable display label for this property
 * @param type  the data type of this property (e.g., "string", "number",
 *              "boolean", "date")
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactProperty(String key, String label, String type) {
    /**
     * Creates a new builder for {@link ContactProperty}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder for {@link ContactProperty}. */
    public static final class Builder {
        private String key;
        private String label;
        private String type;

        private Builder() {
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public ContactProperty build() {
            return new ContactProperty(key, label, type);
        }
    }
}
