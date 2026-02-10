package com.telos.loops.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = false)
public record Contact(
    String id,
    String email,
    String firstName,
    String lastName,
    String source,
    Boolean subscribed,
    String userGroup,
    String userId,
    Map<String, Boolean> mailingLists,
    String optInStatus) {}
