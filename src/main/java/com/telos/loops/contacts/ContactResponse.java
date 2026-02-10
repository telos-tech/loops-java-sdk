package com.telos.loops.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response from creating, updating, or deleting a contact.
 *
 * <p>This response confirms the operation and provides the contact's ID and a status message.
 *
 * @param id the unique identifier for the contact (for create/update operations)
 * @param message a status message describing the result (e.g., "Contact created", "Contact
 *     updated")
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactResponse(String id, String message) {}
