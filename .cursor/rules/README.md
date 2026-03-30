# Cursor Rules – Contentstack Java CDA SDK

This directory contains Cursor AI rules that apply when working in this repository. Rules provide persistent context so the AI follows project conventions and Contentstack CDA patterns.

## How rules are applied

- **File-specific rules** use the `globs` frontmatter: they apply when you open or edit files matching that pattern.
- **Always-on rules** use `alwaysApply: true`: they are included in every conversation in this project.

## Rule index

| File | Applies when | Purpose |
|------|--------------|---------|
| **dev-workflow.md** | (Reference only; no glob) | Core development workflow: branches, running tests, PR expectations. Read for process guidance. |
| **java.mdc** | Editing any `**/*.java` file | Java 8 standards: naming, package layout, `com.contentstack.sdk` structure, logging, Lombok/annotations, avoiding raw types. |
| **contentstack-java-cda.mdc** | Editing `src/main/java/com/contentstack/sdk/**/*.java` | CDA-specific patterns: Stack/Config, host/version/region/branch, RetryOptions/RetryInterceptor, callbacks, alignment with Content Delivery API. |
| **testing.mdc** | Editing `src/test/**/*.java` | Testing patterns: JUnit 5, unit vs integration naming (`Test*` vs `*IT`), BaseIntegrationTest, timeouts, JaCoCo. |
| **code-review.mdc** | Always | PR/review checklist: API stability, error handling, backward compatibility, dependencies and security (e.g. SCA). |

## Related

- **AGENTS.md** (repo root) – Main entry point for AI agents: project overview, entry points, and pointers to rules and skills.
- **skills/** – Reusable skill docs (Contentstack Java CDA, testing, code review, framework) for deeper guidance on specific tasks.
