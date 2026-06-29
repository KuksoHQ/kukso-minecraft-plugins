# Sprint Documentation

`docs/sprints/` is the canonical planning and delivery record for LLM-assisted and human development sessions in this repository. The process is intentionally lightweight Scrum: plan enough to make work traceable, keep records concise, and update the sprint as the work changes.

Sprints are monorepo-wide. Individual sprint items should identify their module or repository area, such as `:lib`, `:dialogs`, `:worlds`, `:items`, `build-logic`, or `docs`.

## Cadence

Use 1-week sprints by default. Real sprint files should use this filename pattern:

```text
docs/sprints/YYYY-MM-DD-sprint-N.md
```

Use `docs/sprints/templates/sprint.md` when starting a new sprint.

## Backlog Grooming

Use `docs/sprints/backlog.md` for candidate work before sprint commitment. Keep backlog items concise but implementation-ready:

- Module or area affected.
- User value or reason the work matters.
- Acceptance criteria.
- Expected validation commands or manual checks.

Move items between `Ready`, `Candidate`, `Blocked`, and `Done` as understanding changes.

## Sprint Planning

At sprint start, define:

- Sprint goal.
- Dates and sprint number.
- Capacity or assumptions.
- Committed items linked back to backlog entries when useful.
- Acceptance criteria for each committed item.
- Validation expectations, including the smallest relevant Gradle command when code changes are involved.

Each non-trivial code or documentation task should be tied to a sprint item before implementation starts.

## Release Tracking

Sprint docs may record intended release targets and changelog impact, but `CHANGELOG.md` is the canonical release history and release-to-sprint index.

Use this direction of traceability:

- Sprint item: "This work is intended for `Unreleased` or a future module release."
- Changelog release entry: "This shipped release was produced by these sprint docs."

This keeps the sprint useful during execution while keeping final release history stable after delivery.

## Daily And Update Notes

Use the sprint work log to record meaningful updates, not every small edit. Capture:

- Progress that changes item status.
- Blockers or external dependencies.
- Scope changes and why they happened.
- Decisions that future agents or maintainers need to understand.
- Verification results and relevant command output summaries.

## Sprint Review

At sprint close or delivery time, summarize:

- Completed work.
- Verification performed.
- Shipped artifacts, changed modules, config notes, or permission changes.
- Changelog status for release-relevant work.
- Items that were not completed and why.

## Retrospective

Keep the retro short. Record what to keep, what to change next time, and follow-up candidates for the next sprint or backlog.

## Legacy Notes

`docs/implementation-history/` remains available for old records. Do not add new entries there unless the user explicitly requests legacy implementation-history documentation.
