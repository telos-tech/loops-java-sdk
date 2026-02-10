package com.telos.loops.internal.mappers;

import com.telos.loops.properties.ContactProperty;
import com.telos.loops.properties.ContactPropertyCreateRequest;
import com.telos.loops.properties.ContactPropertyResponse;

public class ContactPropertiesMapper {

  // ============================================================
  // Request Mappings: Public API → Generated OpenAPI Models
  // ============================================================

  public static com.telos.loops.internal.openapi.model.ContactPropertyCreateRequest toGenerated(
      ContactPropertyCreateRequest request) {
    var generated = new com.telos.loops.internal.openapi.model.ContactPropertyCreateRequest();
    generated.setName(request.name());
    generated.setType(request.type());
    return generated;
  }

  // ============================================================
  // Response Mappings: Generated OpenAPI Models → Public API
  // ============================================================

  public static ContactPropertyResponse fromGenerated(
      com.telos.loops.internal.openapi.model.ContactPropertySuccessResponse response) {
    return new ContactPropertyResponse(response.getSuccess());
  }

  public static ContactProperty fromGenerated(
      com.telos.loops.internal.openapi.model.ContactProperty generated) {
    return new ContactProperty(generated.getKey(), generated.getLabel(), generated.getType());
  }
}
