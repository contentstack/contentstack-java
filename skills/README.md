# Skills – Contentstack Java CDA SDK

This directory contains **skills**: reusable guidance for AI agents (and developers) on specific tasks. Each skill is a folder with a `SKILL.md` file.

## When to use which skill

| Skill | Use when |
|-------|----------|
| **contentstack-java-cda** | Implementing or changing CDA features: Stack/Config, entries, assets, content types, sync, taxonomy, HTTP layer, retry, callbacks, error handling. |
| **testing** | Writing or refactoring tests: JUnit 5, unit vs integration layout, BaseIntegrationTest, timeouts, Credentials/.env, JaCoCo. |
| **code-review** | Reviewing a PR or preparing your own: API design, null-safety, exceptions, backward compatibility, dependencies/security, test coverage. |
| **framework** | Touching config, retry, or the HTTP layer: Config options, RetryOptions/RetryInterceptor, CSHttpConnection, connection/request flow, error handling. |

## How agents should use skills

- **contentstack-java-cda:** Apply when editing SDK core (`com.contentstack.sdk`) or adding CDA-related behavior. Follow Stack/Config, CDA API alignment, and existing callback/error patterns.
- **testing:** Apply when creating or modifying test classes. Follow naming (`Test*` / `*IT`), BaseIntegrationTest for integration tests, and existing Surefire/JaCoCo setup.
- **code-review:** Apply when performing or simulating a PR review. Go through the checklist (API stability, errors, compatibility, dependencies, tests) and optional severity levels.
- **framework:** Apply when changing Config, RetryOptions, RetryInterceptor, or CSHttpConnection. Keep retry and connection behavior consistent with the rest of the SDK.

Each skill’s `SKILL.md` contains more detailed instructions and references.
