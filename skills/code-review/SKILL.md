---
name: code-review
description: Use when reviewing PRs or before opening a PR – API design, null-safety, exceptions, backward compatibility, dependencies, security, and test quality
---

# Code Review – Contentstack Java CDA SDK

Use this skill when performing or preparing a pull request review for the Java CDA SDK.

## When to use

- Reviewing someone else’s PR.
- Self-reviewing your own PR before submission.
- Checking that changes meet project standards (API, errors, compatibility, tests, security).

## Instructions

Work through the checklist below. Optionally tag items with severity: **Blocker**, **Major**, **Minor**.

### 1. API design and stability

- [ ] **Public API:** New or changed public methods/classes are necessary and documented (Javadoc with purpose and, where relevant, params/returns).
- [ ] **Backward compatibility:** No breaking changes to public API unless explicitly agreed (e.g. major version). Deprecations should have alternatives and timeline.
- [ ] **Naming:** Consistent with existing SDK style and CDA terminology (e.g. `Stack`, `Entry`, `Query`, `Config`).

**Severity:** Breaking public API without approval = Blocker. Missing docs on new public API = Major.

### 2. Error handling and robustness

- [ ] **Errors:** API failures are mapped to the SDK `Error` type and passed through existing callback/result patterns (e.g. `ResultCallBack`).
- [ ] **Null safety:** No unintended NPEs; document or annotate (e.g. `@NotNull`/`@Nullable`) where it matters for public API.
- [ ] **Exceptions:** Checked exceptions are handled or declared; use of unchecked exceptions is intentional and documented where relevant.

**Severity:** Wrong or missing error handling in new code = Major.

### 3. Dependencies and security

- [ ] **Dependencies:** New or upgraded dependencies are justified. Version bumps are intentional and do not introduce known vulnerabilities.
- [ ] **SCA:** Any security findings (e.g. Snyk, Dependabot) in the change set are addressed or explicitly deferred with a ticket.

**Severity:** New critical/high vulnerability = Blocker.

### 4. Testing

- [ ] **Coverage:** New or modified behavior has corresponding unit and/or integration tests.
- [ ] **Conventions:** Tests follow naming (`Test*` unit, `*IT` integration) and use `BaseIntegrationTest` for integration tests where appropriate.
- [ ] **Quality:** Tests are readable, deterministic (no flakiness), and assert meaningful behavior.

**Severity:** No tests for new behavior = Blocker. Flaky or weak tests = Major.

### 5. Optional severity summary

- **Blocker:** Must fix before merge (e.g. breaking API without approval, security issue, no tests for new code).
- **Major:** Should fix (e.g. inconsistent error handling, missing Javadoc on new public API, flaky tests).
- **Minor:** Nice to fix (e.g. style, minor docs, redundant code).

## References

- Project rule: `.cursor/rules/code-review.mdc`
- Testing skill: `skills/testing/SKILL.md` for test standards
