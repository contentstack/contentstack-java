# Contentstack Java Delivery SDK – Agent guide

**Universal entry point** for contributors and AI agents. Detailed conventions live in **`skills/*/SKILL.md`**.

## What this repo is

| Field | Detail |
|--------|--------|
| **Name:** | [contentstack-java](https://github.com/contentstack/contentstack-java) (`com.contentstack.sdk:java`) |
| **Purpose:** | Java SDK for the Contentstack Content Delivery API (stack, entries, assets, queries, sync, etc.). |
| **Out of scope:** | Not the Android or Swift SDKs—use those repositories for mobile-specific clients. |

## Tech stack (at a glance)

| Area | Details |
|------|---------|
| Language | Java **8** (`maven.compiler.source/target` in `pom.xml`) |
| Build | Maven (`pom.xml`); JaCoCo for coverage |
| Tests | JUnit 5; unit tests and `*IT` integration tests under `src/test/java/` |
| Lint / coverage | JaCoCo (`jacoco:report` in CI); no separate Checkstyle in quick path—follow existing style |
| CI | `.github/workflows/unit-testing.yml`, `check-branch.yml`, `sca-scan.yml`, `policy-scan.yml`, `codeql-analysis.yml`, publish workflows |

## Commands (quick reference)

| Command type | Command |
|--------------|---------|
| Build / test | `mvn clean test -Dgpg.skip=true` (adjust `-Dtest` per `pom.xml` surefire notes) |
| Coverage (CI-style) | `mvn clean test -Dtest='Test*' jacoco:report -Dgpg.skip=true` |

## Where the documentation lives: skills

| Skill | Path | What it covers |
|-------|------|----------------|
| **Development workflow** | [`skills/dev-workflow/SKILL.md`](skills/dev-workflow/SKILL.md) | Branches, Maven, CI, JaCoCo |
| **Java CDA SDK** | [`skills/contentstack-java-cda/SKILL.md`](skills/contentstack-java-cda/SKILL.md) | Public API: `Stack`, queries, callbacks, HTTP layer |
| **Java style & layout** | [`skills/java/SKILL.md`](skills/java/SKILL.md) | `src/main/java` packages, Lombok, Retrofit/Rx usage |
| **Framework / HTTP** | [`skills/framework/SKILL.md`](skills/framework/SKILL.md) | Config, RetryOptions, CSHttpConnection, OkHttp/Retrofit flow |
| **Testing** | [`skills/testing/SKILL.md`](skills/testing/SKILL.md) | Unit vs integration tests, resources, credentials |
| **Code review** | [`skills/code-review/SKILL.md`](skills/code-review/SKILL.md) | PR checklist for JVM SDK |

## Using Cursor (optional)

If you use **Cursor**, [`.cursor/rules/README.md`](.cursor/rules/README.md) only points to **`AGENTS.md`**—same docs as everyone else.
