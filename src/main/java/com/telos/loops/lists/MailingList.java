package com.telos.loops.lists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a mailing list in Loops.
 *
 * <p>Mailing lists are used to segment contacts and control which automated emails they receive.
 *
 * @param id the unique identifier for this mailing list
 * @param name the display name of the mailing list
 * @param description a description of the mailing list's purpose
 * @param isPublic whether this mailing list is publicly visible
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record MailingList(String id, String name, String description, boolean isPublic) {}
