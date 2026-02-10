# Loops Java SDK

[![Build Status](https://github.com/telos-tech/loops-java-sdk/actions/workflows/ci.yml/badge.svg)](https://github.com/telos-tech/loops-java-sdk/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/telos-tech/loops-java-sdk/graph/badge.svg?token=CODECOV_TOKEN)](https://codecov.io/gh/telos-tech/loops-java-sdk)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.telos-tech/loops-java-sdk)](https://central.sonatype.com/artifact/io.github.telos-tech/loops-java-sdk)
[![License](https://img.shields.io/github/license/telos-tech/loops-java-sdk)](LICENSE)
![Java](https://img.shields.io/badge/Java-21%2B-blue)
[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://telos-tech.github.io/loops-java-sdk/)

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
    <groupId>io.github.telos-tech</groupId>
    <artifactId>loops-java-sdk</artifactId>
    <version>1.x.y</version>
</dependency>
```

## Documentation

- 📚 [API Reference (Javadoc)](https://telos-tech.github.io/loops-java-sdk/)

## Usage

Initialize the client with your API key:

```java
import com.telos.loops.LoopsClient;

LoopsClient client = LoopsClient.builder()
    .apiKey("your-api-key")
    .build();
```

### Contacts

Create a new contact:

```java
import com.telos.loops.contacts.ContactCreateRequest;

client.contacts().create(
    ContactCreateRequest.builder()
        .email("neil.armstrong@moon.space")
        .firstName("Neil")
        .lastName("Armstrong")
        .userGroup("Astronauts")
        .subscribed(true)
        .putAdditionalProperty("role", "Astronaut")
        .build()
);
```

Find a contact:

```java
import com.telos.loops.contacts.ContactFindRequest;

var contact = client.contacts().find(
    ContactFindRequest.builder()
        .email("neil.armstrong@moon.space")
        .build()
);
```

Update a contact:

```java
import com.telos.loops.contacts.ContactUpdateRequest;

client.contacts().update(
    ContactUpdateRequest.builder()
        .email("neil.armstrong@moon.space")
        .firstName("Neil A.")
        .build()
);
```

Delete a contact:

```java
import com.telos.loops.contacts.ContactDeleteRequest;

client.contacts().delete(
    ContactDeleteRequest.builder()
        .email("neil.armstrong@moon.space")
        .build()
);
```

### Events

Send an event to trigger a loop:

```java
import com.telos.loops.events.EventSendRequest;

client.events().send(
    EventSendRequest.builder()
        .email("neil.armstrong@moon.space")
        .eventName("joinedMission")
        .putEventProperty("mission", "Apollo 11")
        .build()
);
```

### Transactional Emails

Send a transactional email using a pre-defined ID:

```java
import com.telos.loops.transactional.TransactionalSendRequest;

client.transactional().send(
    TransactionalSendRequest.builder()
        .transactionalId("cm...")
        .email("recipient@example.com")
        .putDataVariable("name", "Recipient Name")
        .build()
);
```


## Development

### Prerequisites
- JDK 21+
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
