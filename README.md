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

## Documentation

- 📘 [Getting Started Guide](https://telos-tech.github.io/loops-java-sdk/getting-started.html)
- 📚 [API Reference (JavaDoc)](https://telos-tech.github.io/loops-java-sdk/api/)
- 📖 [Usage Guides](https://telos-tech.github.io/loops-java-sdk/guides/contacts.html)
- 💡 [Code Examples](https://telos-tech.github.io/loops-java-sdk/examples/)

## Quick Start

```java
import com.telos.loops.LoopsClient;
import com.telos.loops.contacts.ContactCreateRequest;
import com.telos.loops.events.EventRequest;
import com.telos.loops.transactional.TransactionalRequest;

public class App {
    public static void main(String[] args) {
        // Initialize the client
        LoopsClient client = LoopsClient.builder()
                .apiKey("your-api-key")
                .build();

        // Create a contact
        client.contacts().create(
            ContactCreateRequest.builder()
                .email("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .build()
        );

        // Send an event
        client.events().send(
            EventRequest.builder()
                .email("user@example.com")
                .eventName("signed_up")
                .build()
        );

        // Send a transactional email
        client.transactional().send(
            TransactionalRequest.builder()
                .transactionalId("welcome_email")
                .email("user@example.com")
                .build()
        );
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
