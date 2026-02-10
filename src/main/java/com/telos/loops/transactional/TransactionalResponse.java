package com.telos.loops.transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = false)
public record TransactionalResponse(boolean success) {}
