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

## Releasing

Releases are automated via GitHub Actions when a new tag is pushed.

1.  **Draft a New Release**: On GitHub, draft a new release.
2.  **Create Tag**: Use semver (e.g., `v1.0.0`).
3.  **Publish**: Publishing the release will trigger the workflow to build, sign, and deploy to Maven Central.

### Required Secrets

The following secrets must be configured in the repository settings for deployment to succeed:

-   `GPG_PRIVATE_KEY`: ASCII-armored private key for signing artifacts.
-   `GPG_PASSPHRASE`: Passphrase for the GPG key.
-   `CENTRAL_TOKEN_USERNAME`: User token for Maven Central (Sonatype).
-   `CENTRAL_TOKEN_PASSWORD`: Password token for Maven Central.
-   `CODECOV_TOKEN`: Token for Codecov upload (optional but recommended for coverage badge).
