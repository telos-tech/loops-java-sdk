package com.telos.loops.apikey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nonnull;

/**
 * Response from testing an API key.
 *
 * <p>This response indicates whether the API key is valid and provides the associated team name.
 *
 * @param success whether the API key is valid and authenticated successfully
 * @param teamName the name of the team associated with this API key
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record ApiKeyTestResponse(@Nonnull Boolean success, @Nonnull String teamName) {}
