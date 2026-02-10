package com.telos.loops.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Request to delete a contact from Loops.
 *
 * <p>You must provide either an email address or a userId to identify the contact to delete.
 * Providing both is allowed but not required.
 *
 * @param email the email address of the contact to delete
 * @param userId your application's unique identifier for the contact to delete
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactDeleteRequest(String email, String userId) {}
