package com.telos.loops.transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = false)
public record TransactionalEmail(
    String id, String name, String lastUpdated, List<String> dataVariables) {}
