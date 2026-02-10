package com.telos.loops.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response from creating a custom contact property.
 *
 * <p>This response confirms whether the property was successfully created.
 *
 * @param success whether the contact property was successfully created
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactPropertyResponse(boolean success) {}
