---
name: code-review
description: Use when reviewing PRs for contentstack-java—API compatibility, tests, Maven, and security.
---

# Code review – contentstack-java

## When to use

- Authoring or reviewing SDK changes
- Evaluating dependency or Retrofit/HTTP changes

## Instructions

### Checklist

- **API**: Public methods and models backward compatible or versioned correctly.
- **Tests**: Unit tests for logic; IT updates if API contracts shift.
- **Build**: `mvn clean test -Dgpg.skip=true` passes; JaCoCo expectations met when coverage workflow applies.
- **Dependencies**: Maven coordinates vetted; no unnecessary surface for consumers.
- **Secrets**: No credentials in source or test resources committed by mistake.

### Severity hints

- **Blocker**: Broken build, failing critical tests, or credential leak.
- **Major**: Missing tests for new public API or risky HTTP changes.
- **Minor**: Javadoc, naming, internal cleanup.
