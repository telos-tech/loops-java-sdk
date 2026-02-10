package com.telos.loops.lists;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)
public record MailingList(String id, String name, String description, boolean isPublic) {}
