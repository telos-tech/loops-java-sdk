package com.telos.loops;

import com.telos.loops.contacts.ContactCreateRequest;
import com.telos.loops.events.EventSendRequest;
import com.telos.loops.properties.ContactPropertyCreateRequest;
import com.telos.loops.transactional.TransactionalSendRequest;
import java.util.Map;

/** Factory methods for creating test data with realistic values. */
public class TestFixtures {

  // Common test data
  public static final String TEST_EMAIL = "user@example.com";
  public static final String TEST_USER_ID = "user-123";
  public static final String TEST_API_KEY = "test-api-key";
  public static final String TEST_FIRST_NAME = "John";
  public static final String TEST_LAST_NAME = "Doe";

  /** Create a minimal contact create request with just email. */
  public static ContactCreateRequest minimalContactCreateRequest() {
    return new ContactCreateRequest(TEST_EMAIL, null, null, false, null, null, null, null);
  }

  /** Create a full contact create request with all fields populated. */
  public static ContactCreateRequest fullContactCreateRequest() {
    return new ContactCreateRequest(
        TEST_EMAIL,
        TEST_FIRST_NAME,
        TEST_LAST_NAME,
        true,
        "premium",
        TEST_USER_ID,
        Map.of("newsletter", true),
        Map.of("customField", "customValue"));
  }

  /** Create a minimal event send request. */
  public static EventSendRequest minimalEventSendRequest() {
    return new EventSendRequest(TEST_EMAIL, null, "Signup", null, null, null);
  }

  /** Create an event send request with properties. */
  public static EventSendRequest eventSendRequestWithProperties() {
    return new EventSendRequest(
        TEST_EMAIL,
        TEST_USER_ID,
        "PurchaseCompleted",
        Map.of("plan", "pro", "amount", 99.99),
        null,
        null);
  }

  /** Create a transactional email send request. */
  public static TransactionalSendRequest minimalTransactionalSendRequest() {
    return new TransactionalSendRequest("transactional-id", TEST_EMAIL, null, null, null);
  }

  /** Create a contact property create request. */
  public static ContactPropertyCreateRequest contactPropertyCreateRequest() {
    return new ContactPropertyCreateRequest("customField", "string");
  }

  /** Create a JSON response for successful contact creation. */
  public static String contactCreateSuccessResponse() {
    return """
        {
          "success": true,
          "id": "contact-123"
        }
        """;
  }

  /** Create a JSON response for successful event sending. */
  public static String eventSendSuccessResponse() {
    return """
        {
          "success": true
        }
        """;
  }

  /** Create a JSON response for rate limit error (429). */
  public static String rateLimitErrorResponse() {
    return """
        {
          "error": "Rate limit exceeded"
        }
        """;
  }

  /** Create a JSON response for validation error (400). */
  public static String validationErrorResponse() {
    return """
        {
          "error": "Invalid email format"
        }
        """;
  }

  /** Create a JSON response for unauthorized error (401). */
  public static String unauthorizedErrorResponse() {
    return """
        {
          "error": "Invalid API key"
        }
        """;
  }

  /** Create a JSON response for server error (500). */
  public static String serverErrorResponse() {
    return """
        {
          "error": "Internal server error"
        }
        """;
  }

  /** Create a JSON response for API key test. */
  public static String apiKeyTestSuccessResponse() {
    return """
        {
          "success": true,
          "teamName": "Test Team"
        }
        """;
  }

  /** Create a JSON response for mailing lists. */
  public static String mailingListsResponse() {
    return """
        [
          {
            "id": "list-1",
            "name": "Newsletter",
            "isPublic": true
          },
          {
            "id": "list-2",
            "name": "Product Updates",
            "isPublic": false
          }
        ]
        """;
  }

  /** Create a JSON response for contact properties. */
  public static String contactPropertiesResponse() {
    return """
        [
          {
            "key": "firstName",
            "type": "string"
          },
          {
            "key": "lastName",
            "type": "string"
          }
        ]
        """;
  }

  /** Create a JSON response for contact property creation. */
  public static String contactPropertyCreateResponse() {
    return """
        {
          "success": true
        }
        """;
  }

  /** Create a JSON response for transactional email. */
  public static String transactionalEmailResponse() {
    return """
        {
          "success": true
        }
        """;
  }

  /** Create a JSON response for transactional list. */
  public static String transactionalListResponse() {
    return """
        {
          "data": [
            {
              "id": "trans-1",
              "name": "Welcome Email",
              "lastUpdated": "2024-01-01T00:00:00Z",
              "dataVariables": []
            }
          ],
          "pagination": {
            "totalPages": 1,
            "totalResults": 1,
            "returnedResults": 1,
            "perPage": 10
          }
        }
        """;
  }

  private TestFixtures() {
    // Utility class
  }
}
