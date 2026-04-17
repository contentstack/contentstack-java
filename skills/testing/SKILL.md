---
name: testing
description: Use for JUnit tests, integration (*IT) tests, fixtures, and credentials handling in contentstack-java.
---

# Testing – contentstack-java

## When to use

- Adding unit tests (`Test*` classes) or integration tests (`*IT`)
- Debugging failures that need stack credentials

## Instructions

### Test types

- **Unit tests** (`Test*` pattern in CI snippet) exercise SDK logic with mocks/fixtures.
- **Integration tests** (`*IT`) may call live services—require valid credentials and environment; see `Credentials.java` and test base classes.

### Running subsets

- Surefire supports `-Dtest=...` patterns documented in `pom.xml`—use to iterate quickly without the full IT suite.

### Safety

- Never commit API keys or delivery tokens; use env/secret patterns already established in tests.
