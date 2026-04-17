---
name: framework
description: Use when touching config, retry, or HTTP layer – Config, RetryOptions, RetryInterceptor, CSHttpConnection, connection/request flow, and error handling
---

# Framework – Contentstack Java CDA SDK

Use this skill when changing configuration, retry behavior, or the HTTP connection layer.

## When to use

- Modifying **Config** options (host, version, region, branch, proxy, connection pool, retry, plugins, early access).
- Changing **RetryOptions** or **RetryInterceptor** (retry limit, delay, backoff, retryable status codes).
- Changing **CSHttpConnection**, request building, or how the SDK uses Retrofit/OkHttp.
- Introducing or changing connection pooling, timeouts, or interceptors.

## Instructions

### Config

- **Config** holds: host (default `cdn.contentstack.io`), version (default `v3`), scheme, endpoint, region (`ContentstackRegion`), branch, live preview settings, proxy, connection pool, **RetryOptions**, management/preview tokens, early access, plugins, releaseId, previewTimestamp.
- Use setter-style APIs (e.g. `setHost`, `setRetryOptions`). Preserve default values where existing behavior depends on them.
- **Reference:** `src/main/java/com/contentstack/sdk/Config.java`.

### RetryOptions and RetryInterceptor

- **RetryOptions:** Configurable retry limit (default 3, max 10), base delay (ms), backoff strategy (e.g. EXPONENTIAL, LINEAR), retryable HTTP status codes (default 408, 429, 502, 503, 504), and a global retry enabled flag. Use builder-style or setters.
- **RetryInterceptor:** OkHttp interceptor that applies retries per `RetryOptions`. Ensure the interceptor is attached to the client used for CDA requests and that it respects the configured options.
- **Custom backoff:** `CustomBackoffStrategy` and `RetryOptions.BackoffStrategy` exist for extensibility; keep custom strategies consistent with the documented behavior.
- **Reference:** `RetryOptions.java`, `RetryInterceptor.java`, `CustomBackoffStrategy.java`.

### CSHttpConnection and request flow

- **CSHttpConnection** builds and executes requests using the configured stack, headers, and controller (e.g. QUERY, ENTRY, ASSET, SYNC). It uses **APIService** (Retrofit) under the hood.
- **Request flow:** Build URL and headers from Config/Constants, set controller and callback, execute via the HTTP layer. Responses and errors are passed to **ResultCallBack** (or specific callback types) and mapped to **Error** when the API returns an error.
- **Connection pool and client:** Config’s connection pool and any proxy are applied to the OkHttp client. Do not bypass the shared client/config when adding new CDA calls.
- **Reference:** `CSHttpConnection.java`, `APIService.java`, `Constants.java`, `IRequestModelHTTP` / `IURLRequestHTTP`.

### Error handling

- Map HTTP and API errors to the **Error** class (errorMessage, errorCode, errDetails). Pass errors through the same callback mechanism used elsewhere so callers get a consistent contract.
- **Reference:** `Error.java`, and callback interfaces that receive errors.

## Key classes

- **Config:** `Config.java`, `Config.ContentstackRegion`
- **Retry:** `RetryOptions.java`, `RetryOptions.BackoffStrategy`, `RetryInterceptor.java`, `CustomBackoffStrategy.java`
- **HTTP:** `CSHttpConnection.java`, `APIService.java`, `CSConnectionRequest.java`, `Constants.java`
- **Errors/callbacks:** `Error.java`, `ResultCallBack`, and related callback types
