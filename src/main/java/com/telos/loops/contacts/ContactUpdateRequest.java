package com.telos.loops.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

/**
 * Request to update an existing contact in Loops.
 *
 * <p>You must provide either an email or userId to identify the contact. Only the fields you
 * provide will be updated; omitted fields remain unchanged. Custom properties can be updated via
 * the additionalProperties map.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * ContactUpdateRequest request = new ContactUpdateRequest(
 *     "user@example.com",  // email (identifier)
 *     "Jane",              // firstName (update)
 *     null,                // lastName (no change)
 *     null,                // subscribed (no change)
 *     "enterprise",        // userGroup (update)
 *     null,                // userId
 *     null,                // mailingLists (no change)
 *     Map.of("planName", "Enterprise")  // additionalProperties (update)
 * );
 * }</pre>
 *
 * @param email the email address of the contact to update (used as identifier or to update email)
 * @param firstName the contact's first name (null to leave unchanged)
 * @param lastName the contact's last name (null to leave unchanged)
 * @param subscribed whether the contact should be subscribed (null to leave unchanged)
 * @param userGroup the user group to assign (null to leave unchanged)
 * @param userId your application's unique identifier (used as identifier or to update userId)
 * @param mailingLists map of mailing list IDs to subscription status (null to leave unchanged)
 * @param additionalProperties custom contact properties to update (null to leave unchanged)
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactUpdateRequest(
    String email,
    String firstName,
    String lastName,
    Boolean subscribed,
    String userGroup,
    String userId,
    Map<String, Boolean> mailingLists,
    Map<String, Object> additionalProperties) {}
