package com.telos.loops.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telos.loops.error.LoopsValidationException;
import jakarta.annotation.Nonnull;
import java.util.regex.Pattern;

@JsonIgnoreProperties(ignoreUnknown = false)
public record ContactPropertyCreateRequest(@Nonnull String name, @Nonnull String type) {

  private static final Pattern CAMEL_CASE_PATTERN = Pattern.compile("^[a-z][a-zA-Z0-9]*$");
  private static final String[] VALID_TYPES = {"string", "number", "boolean", "date"};

  public ContactPropertyCreateRequest {
    // Validate name is camelCase
    if (name == null || name.isBlank()) {
      throw new LoopsValidationException("name is required and cannot be blank");
    }
    if (!CAMEL_CASE_PATTERN.matcher(name).matches()) {
      throw new LoopsValidationException(
          "name must be in camelCase format (e.g., 'planName'), got: " + name);
    }

    // Validate type
    if (type == null || type.isBlank()) {
      throw new LoopsValidationException("type is required and cannot be blank");
    }
    boolean validType = false;
    for (String validTypeValue : VALID_TYPES) {
      if (validTypeValue.equals(type)) {
        validType = true;
        break;
      }
    }
    if (!validType) {
      throw new LoopsValidationException(
          "type must be one of: string, number, boolean, date. Got: " + type);
    }
  }
}
