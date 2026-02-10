# Contributing to Loops Java SDK

Thank you for your interest in contributing! We welcome bug reports, feature requests, and pull requests.

## Code of Conduct
Please communicate with respect and professionalism.

## Getting Started
1. **Fork** the repository.
2. **Clone** your fork.
3. Install **Java 17** and **Maven**.

## Development Workflow
1. Create a new branch: `git checkout -b feature/my-feature`
2. Make your changes.
3. Verify builds and tests:
   ```bash
   mvn clean verify
   ```
   This will run:
   - Unit tests
   - Checkstyle (linting)
   - Spotless (formatting)

## Coding Standards
- We follow **Google Java Style**.
- Code is automatically formatted via `spotless`.
- Run `mvn spotless:apply` to fix formatting issues automatically.

## Submitting Changes
1. Push your branch to GitHub.
2. Open a **Pull Request** against `main`.
3. Ensure CI checks pass.

## Reporting Issues
Please include:
- SDK version
- Reproduction steps or code snippet
- Expected vs actual behavior
