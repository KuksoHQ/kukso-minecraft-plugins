# Product Backlog

Use this file for ongoing product and maintenance backlog items. Keep entries short enough to groom quickly, but specific enough that an agent can implement them without rediscovering the goal.

## Ready

- [ ] **Module/area:** All plugin modules
  - **User value:** Regressions in parsing, validation, and command utilities are caught by `./gradlew test` instead of manual Paper server checks.
  - **Acceptance criteria:** Each module with testable parsing/validation/command-utility logic gets a seeded `modules/<module>/src/test/java` tree with at least one focused `*Test` class covering real behavior (no placeholder asserts), and `./gradlew test` runs them green.
  - **Validation:** `./gradlew test` and a review that the covered logic matches AGENTS.md testing guidelines.

## Candidate

- _No candidate items yet._

## Blocked

- _No blocked items yet._

## Done

- [x] **Module/area:** Repository exit workflow
  - **User value:** Completed work exits the repo through a predictable PR, versioning, changelog, tag, artifact, and release process.
  - **Acceptance criteria:** `AGENTS.md`, `docs/releases/`, the PR template, changelog rules, sprint record, and release workflow define per-module SemVer, Conventional scoped commits, PR-first delivery, release prep PRs, and manual per-module GitHub Action releases.
  - **Validation:** Run `git diff --check -- AGENTS.md CHANGELOG.md docs/sprints docs/releases .github` and review the release workflow YAML.

- [x] **Module/area:** Repository workflow documentation
  - **User value:** Historical implementation work is traceable through the current sprint workflow instead of split across legacy implementation-history notes.
  - **Acceptance criteria:** Convert `docs/implementation-history/2026-06-07-kukso-worlds-localization-fix.md` into `docs/sprints/2026-06-07-sprint-1.md`, preserve the legacy plan, implementation, verification, and notes, and remove the old file to avoid duplicate records.
  - **Validation:** Read the converted sprint document and run `git diff --check -- docs/sprints CHANGELOG.md`.

- [x] **Module/area:** Repository product and release documentation
  - **User value:** Future work has a clear product vision source and a release record that links shipped versions back to sprint execution.
  - **Acceptance criteria:** Root `PRODUCT.md` defines product vision and plugin feel, including runtime-reloadable config expectations, root `CHANGELOG.md` tracks release-relevant changes, and `AGENTS.md` explains how product, sprint, changelog, and config-reload guidance relate.
  - **Validation:** Read the created Markdown files and confirm `git status --short` only shows intended documentation changes plus any pre-existing user changes.

- [x] **Module/area:** Repository workflow documentation
  - **User value:** Future LLM-assisted sessions follow a consistent lightweight Scrum process instead of writing ad hoc implementation-history notes.
  - **Acceptance criteria:** `AGENTS.md` points future agents to `docs/sprints/`, sprint workflow docs exist, a backlog exists, a reusable sprint template exists, and `docs/implementation-history/` is retired.
  - **Validation:** Read the created Markdown files and confirm `git status --short` only shows the intended documentation changes plus any pre-existing user changes.
