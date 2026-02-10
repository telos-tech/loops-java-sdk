package com.telos.loops.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactFindRequest(String email, String userId) {}
