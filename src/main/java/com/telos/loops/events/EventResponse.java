package com.telos.loops.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response from sending an event to Loops.
 *
 * <p>This response confirms whether the event was successfully recorded.
 *
 * @param success whether the event was successfully sent and recorded
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record EventResponse(boolean success) {}
