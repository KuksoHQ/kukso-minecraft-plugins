# Product Backlog

Use this file for ongoing product and maintenance backlog items. Keep entries short enough to groom quickly, but specific enough that an agent can implement them without rediscovering the goal.

## Ready

- _No ready items._

## Candidate

- [ ] **Module/area:** `:dialogs`, `:items`
  - **User value:** Non-English players get localized dialog and item text consistent with `:lib` and `:worlds`.
  - **Acceptance criteria:** Add `lang/` files (at minimum `en`, `tr`) for the `:dialogs` and `:items` modules, matching the existing `lang/` structure and key conventions used in `:lib` and `:worlds`.
  - **Validation:** Manual Paper server check with a non-English locale, plus a review that lang keys match usages in code.

- [ ] **Module/area:** `ci`
  - **User value:** Style and formatting regressions are caught automatically instead of relying on manual review.
  - **Acceptance criteria:** Add lint/format checks to `.github/workflows/build.yml` (e.g. a Java formatter/checkstyle step) that fail the workflow on violations.
  - **Validation:** Push a deliberately misformatted change to a branch and confirm the workflow fails; confirm it passes on clean code.

- [ ] **Module/area:** `:dialogs`, `:dialogs-exp-config-addon`
  - **User value:** Simpler, less error-prone build wiring between the dialogs module and its example addon.
  - **Acceptance criteria:** Evaluate replacing the embedded exp-config addon jar (currently bundled into `:dialogs` as a resource) with a proper Gradle project dependency or an explicit copy task, and document the tradeoffs before implementing.
  - **Validation:** N/A until scoped; this item is evaluation-only until a decision is made.

- [ ] **Module/area:** `docs`
  - **User value:** Windows contributors are not silently blocked or given a broken checkout by the `.claude/skills` symlink.
  - **Acceptance criteria:** Document that Windows contributors need `git config core.symlinks=true` (and Developer Mode or admin rights) for the `.agents/skills` -> `.claude/skills` symlink to check out correctly; add this note to the relevant contributor-facing doc.
  - **Validation:** Read-through of the added documentation note.

## Blocked

- _No blocked items yet._

## Done

- [x] **Module/area:** All plugin modules
  - **User value:** Regressions in parsing, validation, and command utilities are caught by `./gradlew test` instead of manual Paper server checks.
  - **Acceptance criteria:** Each module with testable parsing/validation/command-utility logic gets a seeded `modules/<module>/src/test/java` tree with at least one focused `*Test` class covering real behavior (no placeholder asserts), and `./gradlew test` runs them green.
  - **Validation:** Delivered in `2026-07-03-sprint-3.md`; `./gradlew test` green with 16 tests across five modules.

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
