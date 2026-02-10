package com.telos.loops.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactUpdateRequest(
    String email,
    String firstName,
    String lastName,
    Boolean subscribed,
    String userGroup,
    String userId,
    Map<String, Boolean> mailingLists,
    Map<String, Object> additionalProperties) {}
