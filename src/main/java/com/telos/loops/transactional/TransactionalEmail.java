package com.telos.loops.transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Represents a transactional email template in Loops.
 *
 * <p>
 * Transactional emails are one-off emails sent in response to user actions
 * (e.g., password
 * reset, order confirmation). Each template is identified by an ID and can
 * accept dynamic data
 * variables.
 *
 * @param id            the unique identifier for this transactional email
 *                      template
 * @param name          the display name of the transactional email
 * @param lastUpdated   the timestamp when this template was last modified
 * @param dataVariables list of variable names that can be used in this template
 *                      (e.g.,
 *                      "resetLink", "orderNumber")
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record TransactionalEmail(
        String id, String name, String lastUpdated, List<String> dataVariables) {

    public TransactionalEmail {
        dataVariables = dataVariables == null ? java.util.Collections.emptyList() : List.copyOf(dataVariables);
    }

    /**
     * Creates a new builder for {@link TransactionalEmail}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder for {@link TransactionalEmail}. */
    public static final class Builder {
        private String id;
        private String name;
        private String lastUpdated;
        private java.util.List<String> dataVariables;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder lastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public Builder dataVariables(List<String> dataVariables) {
            this.dataVariables = dataVariables;
            return this;
        }

        public Builder addDataVariable(String dataVariable) {
            if (this.dataVariables == null) {
                this.dataVariables = new java.util.ArrayList<>();
            }
            this.dataVariables.add(dataVariable);
            return this;
        }

        public TransactionalEmail build() {
            return new TransactionalEmail(id, name, lastUpdated, dataVariables);
        }
    }
}
