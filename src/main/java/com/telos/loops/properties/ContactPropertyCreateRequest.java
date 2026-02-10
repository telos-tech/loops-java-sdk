package com.telos.loops.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telos.loops.error.LoopsValidationException;
import jakarta.annotation.Nonnull;
import java.util.regex.Pattern;

/**
 * Request to create a new custom contact property in Loops.
 *
 * <p>Custom properties must be defined before they can be used on contacts. The property name must
 * be in camelCase format, and the type must be one of: "string", "number", "boolean", or "date".
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * ContactPropertyCreateRequest request = new ContactPropertyCreateRequest(
 *     "planName",  // name (must be camelCase)
 *     "string"     // type (string, number, boolean, or date)
 * );
 * }</pre>
 *
 * @param name the property name in camelCase format (e.g., "planName", "signupDate")
 * @param type the data type: "string", "number", "boolean", or "date"
 * @throws LoopsValidationException if name is not in camelCase format or type is invalid
 */
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
