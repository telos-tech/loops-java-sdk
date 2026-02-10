package com.telos.loops.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nonnull;
import java.util.Map;

/**
 * Request to create a new contact in Loops.
 *
 * <p>At minimum, you must provide an email address. All other fields are optional. Custom
 * properties can be included via the additionalProperties map.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * ContactCreateRequest request = new ContactCreateRequest(
 *     "user@example.com",  // email (required)
 *     "John",              // firstName
 *     "Doe",               // lastName
 *     true,                // subscribed
 *     "premium",           // userGroup
 *     "user123",           // userId
 *     Map.of("newsletter", true),  // mailingLists
 *     Map.of("planName", "Pro")    // additionalProperties
 * );
 * }</pre>
 *
 * @param email the contact's email address (required)
 * @param firstName the contact's first name
 * @param lastName the contact's last name
 * @param subscribed whether the contact should be subscribed (defaults to true)
 * @param userGroup the user group to assign this contact to
 * @param userId your application's unique identifier for this contact
 * @param mailingLists map of mailing list IDs to subscription status (true to subscribe)
 * @param additionalProperties custom contact properties (must match property names defined in
 *     Loops)
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactCreateRequest(
    @Nonnull String email,
    String firstName,
    String lastName,
    boolean subscribed,
    String userGroup,
    String userId,
    Map<String, Boolean> mailingLists,
    Map<String, Object> additionalProperties) {}
