# Loops Java SDK

![CI](https://github.com/telos/loops-java-sdk/actions/workflows/ci.yml/badge.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

A robust, type-safe Java SDK for the [Loops.so](https://loops.so) API.

## Features

- **Full API Coverage**: Contacts, Events, Transactional Emails, Custom Properties, Mailing Lists, etc.
- **Async Support**: All operations available as `CompletableFuture`.
- **Type-Safe**: Strong typing for all requests and responses.
- **Resilient**: Built-in retry logic for rate limits (429).
- **Observable**: SLF4J logging integration.

## Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.telos</groupId>
    <artifactId>loops-java-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

```java
import com.telos.loops.LoopsClient;
import com.telos.loops.contacts.ContactUpdateRequest;

public class App {
    public static void main(String[] args) {
        LoopsClient client = LoopsClient.builder()
                .apiKey("your-api-key")
                .build();

        // Create a contact
        client.contacts().create(new ContactUpdateRequest("user@example.com", "user_123"));
        
        // Send an event
        client.events().send(new EventSendRequest("user@example.com", "Signup"));
    }
}
```

## Development

### Prerequisites
- JDK 17+
- Maven 3.8+

### Building
```bash
mvn clean verify
```

### Code Style
This project follows Google's Java Style Guide.

- **SpotBugs**: `mvn spotbugs:check`
- **Auto-format**: `mvn spotless:apply`

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## License

MIT License. See [LICENSE](LICENSE) for details.
