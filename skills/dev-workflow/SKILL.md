---
name: dev-workflow
description: Use for Maven lifecycle, CI, JaCoCo, and branch expectations in contentstack-java.
---

# Development workflow – contentstack-java

## When to use

- Running builds/tests locally to match CI
- Preparing a release or snapshot with signing/publish plugins

## Instructions

### Branches

- Integration branches include **`development`**, **`staging`**, and **`master`**—confirm target branch for your PR against team policy.

### Commands

- Typical test run: `mvn clean test -Dgpg.skip=true` (GPG skipped for local dev).
- CI coverage workflow enables tests via `pom.xml` manipulation—see `.github/workflows/unit-testing.yml` for the exact Maven line.
- JDK **8** (Temurin) is used in coverage CI—match for parity when debugging Java-8-specific issues.

### JaCoCo

- Reports land under `target/site/jacoco/` after `jacoco:report`—use HTML output to inspect coverage gaps.
