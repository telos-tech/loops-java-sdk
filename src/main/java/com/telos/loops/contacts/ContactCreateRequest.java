package com.telos.loops.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nonnull;
import java.util.Map;

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
