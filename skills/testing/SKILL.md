---
name: testing
description: Use when writing or refactoring tests – JUnit 5, unit vs integration, BaseIntegrationTest, JaCoCo, naming, and test data
---

# Testing – Contentstack Java CDA SDK

Use this skill when adding or refactoring tests in the Java CDA SDK.

## When to use

- Writing new unit or integration tests.
- Refactoring test layout, base classes, or test utilities.
- Adjusting test configuration (e.g. timeouts, filters) or coverage.

## Instructions

### JUnit 5 and layout

- Use **JUnit 5 (Jupiter)** only. Dependencies and version are in `pom.xml` (junit-jupiter).
- **Unit tests:** Class name must start with **`Test`** (e.g. `TestEntry`, `TestConfig`). Place under `src/test/java/com/contentstack/sdk/`.
- **Integration tests:** Class name must end with **`IT`** (e.g. `ContentstackIT`, `SyncStackIT`). Same package. Extend **`BaseIntegrationTest`** when tests need a real stack and shared setup.

### BaseIntegrationTest and timeouts

- **BaseIntegrationTest** provides:
  - `stack` from `Credentials.getStack()` (uses `.env`).
  - Timeout constants: `DEFAULT_TIMEOUT_SECONDS`, `PERFORMANCE_TIMEOUT_SECONDS`, `LARGE_DATASET_TIMEOUT_SECONDS`.
  - Lifecycle: `@BeforeAll` for setup, optional logging.
- Use **`Assertions.assertTimeout(Duration.ofSeconds(n), () -> { ... })`** for async or long-running operations so tests don’t hang.
- Reference: `src/test/java/com/contentstack/sdk/BaseIntegrationTest.java`.

### Test data and credentials

- **Credentials:** Use **`Credentials`** for integration tests. Require a `.env` (or equivalent) with stack API key, delivery token, and environment. Do not commit real credentials; document required variables.
- **Fixtures:** Reuse existing test assets (e.g. under `src/test/` or `target/test-classes/`) where applicable. Use **`TestHelpers`** and other test utils when present.

### Naming and structure

- One test class per production class when possible (e.g. `TestEntry` for `Entry`).
- Use **`@DisplayName`** for readable test names. Use **`@TestInstance(Lifecycle.PER_CLASS)`** when sharing instance state (e.g. in base integration class).

### Coverage and execution

- **JaCoCo** is configured in `pom.xml`. Reports are generated on `mvn test` (e.g. `target/site/jacoco/`). Some classes are excluded from coverage (see jacoco-maven-plugin `excludes`).
- **Surefire:** Default runs all tests. Use `-Dtest=TestEntry` for one class, `-Dtest='*IT'` for integration only, `-Dtest='Test*'` for unit only.
- Maintain or improve coverage when changing production code; add tests for new or modified behavior.

## References

- `pom.xml` – Surefire and JaCoCo plugin configuration
- `BaseIntegrationTest.java`, `Credentials.java`, `TestHelpers.java`
- Project rule: `.cursor/rules/testing.mdc`
