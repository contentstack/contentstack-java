---
name: contentstack-java-cda
description: Use for the Java Content Delivery API—Stack, Entry, Query, sync, and HTTP/callback patterns.
---

# Java CDA SDK – contentstack-java

## When to use

- Changing public classes under `com.contentstack.sdk`
- Working with Retrofit-backed calls, RxJava flows, or Gson models

## Instructions

### Entry points

- Core usage centers on **`Stack`** and related types (`Query`, `Entry`, `Asset`, sync APIs)—preserve binary compatibility for public methods when possible.

### Dependencies

- Networking uses **Retrofit/OkHttp**-style stack per `pom.xml`; **Gson** for JSON—keep model classes consistent with API responses.

### Semver

- Treat additive changes as minor; breaking signatures or behavior need a **major** bump and migration notes.
