package com.telos.loops.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactProperty(String key, String label, String type) {}
