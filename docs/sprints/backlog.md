# Product Backlog

Use this file for ongoing product and maintenance backlog items. Keep entries short enough to groom quickly, but specific enough that an agent can implement them without rediscovering the goal.

## Ready

- _No ready items yet._

## Candidate

- _No candidate items yet._

## Blocked

- _No blocked items yet._

## Done

- [x] **Module/area:** Repository product and release documentation
  - **User value:** Future work has a clear product vision source and a release record that links shipped versions back to sprint execution.
  - **Acceptance criteria:** Root `PRODUCT.md` defines product vision and plugin feel, including runtime-reloadable config expectations, root `CHANGELOG.md` tracks release-relevant changes, and `AGENTS.md` explains how product, sprint, changelog, and config-reload guidance relate.
  - **Validation:** Read the created Markdown files and confirm `git status --short` only shows intended documentation changes plus any pre-existing user changes.

- [x] **Module/area:** Repository workflow documentation
  - **User value:** Future LLM-assisted sessions follow a consistent lightweight Scrum process instead of writing ad hoc implementation-history notes.
  - **Acceptance criteria:** `AGENTS.md` points future agents to `docs/sprints/`, sprint workflow docs exist, a backlog exists, a reusable sprint template exists, and `docs/implementation-history/` is marked as legacy reference only.
  - **Validation:** Read the created Markdown files and confirm `git status --short` only shows the intended documentation changes plus any pre-existing user changes.
