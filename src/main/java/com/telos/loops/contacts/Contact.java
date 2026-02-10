package com.telos.loops.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

/**
 * Represents a contact in Loops.
 *
 * <p>A contact contains profile information, subscription status, and mailing list memberships.
 * This is returned when finding or listing contacts.
 *
 * @param id the unique identifier for this contact
 * @param email the contact's email address
 * @param firstName the contact's first name
 * @param lastName the contact's last name
 * @param source how this contact was added (e.g., "API", "Import")
 * @param subscribed whether the contact is subscribed to receive emails
 * @param userGroup the user group this contact belongs to
 * @param userId your application's unique identifier for this contact
 * @param mailingLists map of mailing list IDs to subscription status (true if subscribed)
 * @param optInStatus the contact's opt-in status (e.g., "single", "double", "none")
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record Contact(
    String id,
    String email,
    String firstName,
    String lastName,
    String source,
    Boolean subscribed,
    String userGroup,
    String userId,
    Map<String, Boolean> mailingLists,
    String optInStatus) {}
