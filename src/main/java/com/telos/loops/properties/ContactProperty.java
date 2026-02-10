package com.telos.loops.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a custom contact property definition in Loops.
 *
 * <p>Contact properties allow you to store additional data fields on contacts beyond the standard
 * fields (email, firstName, lastName, etc.). Properties must be defined before they can be used.
 *
 * @param key the property's key in camelCase format (e.g., "planName")
 * @param label the human-readable display label for this property
 * @param type the data type of this property (e.g., "string", "number", "boolean", "date")
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactProperty(String key, String label, String type) {}
