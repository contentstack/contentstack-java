# Development Workflow – Contentstack Java CDA SDK

Use this as the standard workflow when contributing to the Java CDA SDK.

## Branches

- Use feature branches for changes (e.g. `feat/...`, `fix/...`).
- Base work off the appropriate long-lived branch (e.g. `staging`, `development`) per team norms.

## Running tests

- **All tests:** `mvn test`
- **Specific test class:** `mvn test -Dtest=TestEntry`
- **Integration tests only:** `mvn test -Dtest='*IT'`
- **Unit tests only:** `mvn test -Dtest='Test*'`

Run tests before opening a PR. Integration tests may require a valid `.env` with stack credentials (see `Credentials` and `BaseIntegrationTest` in the test suite).

## Pull requests

- Ensure the build passes: `mvn clean test`
- Follow the **code-review** rule (see `.cursor/rules/code-review.mdc`) for the PR checklist.
- Keep changes backward-compatible for public API; call out any breaking changes clearly.

## Optional: TDD

If the team uses TDD, follow RED–GREEN–REFACTOR when adding behavior: write a failing test first, then implement to pass, then refactor. The **testing** rule and **skills/testing** skill describe test structure and naming.
