---
name: contentstack-java-cda
description: Use when implementing or changing CDA features – Stack/Config, entries, assets, sync, taxonomy, HTTP, retry, callbacks, and Content Delivery API alignment
---

# Contentstack Java CDA SDK – CDA Implementation

Use this skill when implementing or changing Content Delivery API (CDA) behavior in the Java SDK.

## When to use

- Adding or modifying Stack, Entry, Query, Asset, Content Type, Sync, or Taxonomy behavior.
- Changing Config options (host, version, region, branch, retry).
- Working with the HTTP layer (CSHttpConnection, APIService), retry (RetryOptions, RetryInterceptor), or callbacks and Error handling.

## Instructions

### Stack and Config

- **Entry point:** `Contentstack.stack(apiKey, deliveryToken, environment)`. Optional: pass a `Config` for host, version, region, branch, live preview, retry, proxy, connection pool, early access, plugins.
- **Defaults:** host `cdn.contentstack.io`, version `v3` (see `Config`). Regional endpoints and branch are supported via `Config.setRegion()` and `Config.setBranch()`.
- **Reference:** `Contentstack.java`, `Stack.java`, `Config.java`.

### CDA resources

- **Entries:** `Stack.contentType(uid).entry(uid)`, query APIs, and entry fetch. Use existing `Entry`, `Query`, `EntriesModel`, and callback types.
- **Assets:** `Stack.assetLibrary()`, asset fetch and query. Use `Asset`, `AssetLibrary`, `AssetModel`, and related callbacks.
- **Content types:** Content type schema and listing. Use `ContentType`, `ContentTypesModel`, `ContentTypesCallback`.
- **Sync:** `SyncStack` for sync API. Use existing sync request/response and pagination patterns.
- **Taxonomy:** Taxonomy entries and structure. Use `Taxonomy` and related API surface.
- **Official API:** Align with [Content Delivery API](https://www.contentstack.com/docs/apis/content-delivery-api/) for parameters, response shape, and semantics.

### HTTP and retry

- **HTTP:** All CDA requests go through `CSHttpConnection` and the Retrofit `APIService`. Set headers (e.g. User-Agent, auth) per `Constants` and existing request building.
- **Retry:** Configure via `Config.setRetryOptions(RetryOptions)`. `RetryInterceptor` applies retries per `RetryOptions` (limit, delay, backoff strategy, retryable status codes). Default retryable codes include 408, 429, 502, 503, 504.
- **Reference:** `CSHttpConnection.java`, `APIService.java`, `RetryOptions.java`, `RetryInterceptor.java`.

### Errors and callbacks

- **Errors:** Map API errors to the `Error` class (errorMessage, errorCode, errDetails). Pass to the appropriate callback (e.g. `ResultCallBack`) so callers receive a consistent error shape.
- **Callbacks:** Use existing callback types (`ResultCallBack`, `EntryResultCallBack`, `QueryResultsCallBack`, etc.). Do not change callback contracts without considering backward compatibility.

## Key classes

- **Entry points:** `Contentstack`, `Stack`, `Config`
- **CDA:** `Entry`, `Query`, `Asset`, `AssetLibrary`, `ContentType`, `SyncStack`, `Taxonomy`
- **HTTP / retry:** `CSHttpConnection`, `APIService`, `RetryOptions`, `RetryInterceptor`
- **Errors / results:** `Error`, `QueryResult`, and callback interfaces in `com.contentstack.sdk`

## References

- [Content Delivery API – Contentstack Docs](https://www.contentstack.com/docs/apis/content-delivery-api/)
- Project rules: `.cursor/rules/contentstack-java-cda.mdc`, `.cursor/rules/java.mdc`
