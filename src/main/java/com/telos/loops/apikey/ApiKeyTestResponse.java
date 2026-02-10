package com.telos.loops.apikey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nonnull;

@JsonIgnoreProperties(ignoreUnknown = false)
public record ApiKeyTestResponse(@Nonnull Boolean success, @Nonnull String teamName) {}
