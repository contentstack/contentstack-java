# Contentstack Java CDA SDK – Agent Guide

This document is the main entry point for AI agents working in this repository.

## Project

- **Name:** Contentstack Java CDA SDK (contentstack-java)
- **Purpose:** Java client for the Contentstack **Content Delivery API (CDA)**. It fetches content (entries, assets, content types, sync, taxonomy) from Contentstack and delivers it to Java applications.
- **Repo:** [contentstack-java](https://github.com/contentstack/contentstack-java)

## Tech stack

- **Language:** Java 8 (source/target 1.8)
- **Build:** Maven
- **Testing:** JUnit 5, JaCoCo (coverage)
- **HTTP:** Retrofit 2, OkHttp
- **Other:** Gson, RxJava 3, Lombok, contentstack-utils

## Main entry points

- **`Contentstack`** – Static factory: `Contentstack.stack(apiKey, deliveryToken, environment)` returns a `Stack`.
- **`Stack`** – Main API surface: entries, assets, content types, sync, live preview, etc.
- **`Config`** – Optional configuration: host, version, region, branch, retry, proxy, connection pool, plugins.
- **Paths:** `src/main/java/com/contentstack/sdk/` (production), `src/test/java/com/contentstack/sdk/` (tests).

## Commands

- **Build and test:** `mvn clean test`
- **Single test class:** `mvn test -Dtest=TestEntry`
- **Integration tests only:** `mvn test -Dtest='*IT'`
- **Unit tests only:** `mvn test -Dtest='Test*'`

Integration tests may require a `.env` with stack credentials (see `Credentials` and test README/docs).

## Rules and skills

- **`.cursor/rules/`** – Cursor rules for this repo:
  - **README.md** – Index of all rules.
  - **dev-workflow.md** – Development workflow (branches, tests, PRs).
  - **java.mdc** – Applies to `**/*.java`: Java 8 and SDK conventions.
  - **contentstack-java-cda.mdc** – Applies to SDK core: CDA patterns, Config, HTTP, retry, callbacks.
  - **testing.mdc** – Applies to `src/test/**/*.java`: JUnit 5, Test* / *IT, BaseIntegrationTest, JaCoCo.
  - **code-review.mdc** – Always applied: PR/review checklist.
- **`skills/`** – Reusable skill docs:
  - Use **contentstack-java-cda** when implementing or changing CDA API usage or SDK core behavior.
  - Use **testing** when adding or refactoring tests.
  - Use **code-review** when reviewing PRs or before opening one.
  - Use **framework** when changing config, retry, or HTTP layer (Config, RetryOptions, RetryInterceptor, CSHttpConnection).

Refer to `.cursor/rules/README.md` for when each rule applies and to `skills/README.md` for skill details.
