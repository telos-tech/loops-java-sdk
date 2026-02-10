package com.telos.loops.transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response from sending a transactional email.
 *
 * <p>This response confirms whether the email was successfully queued for delivery.
 *
 * @param success whether the transactional email was successfully sent
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record TransactionalResponse(boolean success) {}
