---
name: java
description: Use for Java package layout, language level (8), and project conventions in contentstack-java.
---

# Java style & layout – contentstack-java

## When to use

- Adding new classes under `src/main/java/com/contentstack/sdk`
- Refactoring tests under `src/test/java`

## Instructions

### Layout

- Sources: **`src/main/java`**; tests and fixtures: **`src/test/java`** and **`src/test/resources`**.
- Java **8** language level—avoid newer language features unless the project explicitly upgrades.

### Libraries

- **Lombok** may appear in sources—ensure IDE/lombok setup for contributors; generated code must still compile under CI.

### Style

- Follow existing patterns in nearby classes (callbacks, builders, null handling)—keep javadoc for public API where the repo already documents.
