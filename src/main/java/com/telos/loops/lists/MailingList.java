package com.telos.loops.lists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a mailing list in Loops.
 *
 * <p>
 * Mailing lists are used to segment contacts and control which automated emails
 * they receive.
 *
 * @param id          the unique identifier for this mailing list
 * @param name        the display name of the mailing list
 * @param description a description of the mailing list's purpose
 * @param isPublic    whether this mailing list is publicly visible
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record MailingList(String id, String name, String description, boolean isPublic) {
    /**
     * Creates a new builder for {@link MailingList}.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder for {@link MailingList}. */
    public static final class Builder {
        private String id;
        private String name;
        private String description;
        private boolean isPublic;

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

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder isPublic(boolean isPublic) {
            this.isPublic = isPublic;
            return this;
        }

        public MailingList build() {
            return new MailingList(id, name, description, isPublic);
        }
    }
}
