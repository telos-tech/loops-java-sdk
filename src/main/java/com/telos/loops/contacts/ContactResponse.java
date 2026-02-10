package com.telos.loops.contacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactResponse(String id, String message) {}
