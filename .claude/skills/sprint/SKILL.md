---
name: sprint
description: "Plan and execute sprint work: groom the backlog, create or update the current sprint file, log progress, and close out items."
---

# Sprint

Plan and execute work using the repository's lightweight Scrum workflow. The canonical record is `docs/sprints/`.

## Instructions

1. Read `docs/sprints/backlog.md` and the most recent `docs/sprints/YYYY-MM-DD-sprint-N.md` file to understand current state. Read `docs/development-cycle.md` (steps 1-4) if you need the full workflow detail.
2. Decide what the user is doing:
   - **Capturing an idea:** add it to `backlog.md` under `Candidate` (not fully shaped) or `Ready` (has user value, acceptance criteria, and validation). Use the entry format shown in `docs/development-cycle.md` step 1. Stop here.
   - **Starting new sprint work:** continue with step 3.
   - **Logging progress or closing out:** skip to step 6.
3. If no sprint file covers today, create `docs/sprints/YYYY-MM-DD-sprint-N.md` from `docs/sprints/templates/sprint.md` (1-week cadence by default). Increment N from the highest existing sprint number.
4. Move the selected backlog item(s) into the sprint as committed items. Every item must name: module or area, user value, release target, changelog impact, acceptance criteria, and validation expectations. Keep items implementation-ready but lightweight.
5. Confirm the sprint goal aligns with `PRODUCT.md`, or state that the sprint is maintenance-only.
6. During implementation, update the sprint Work Log only for meaningful events: status changes, blockers, scope changes, decisions that matter later, and validation pass/fail. Do not log every edit.
7. Run the smallest relevant validation (`./gradlew :module:build`, `git diff --check`, or a docs read-through) and record the result on the item.
8. At close-out: update item status, fill in the Review section (completed items, verification, changelog status, not completed), move finished backlog entries to `Done`, and carry unfinished items back to the backlog or the next sprint.
9. If the work is release-relevant, add a `CHANGELOG.md` entry under `## [Unreleased]` with the module/area label, and add the sprint file under `Sprint Links`. Then follow `/release-prep` when it is time to ship.

## References

- `docs/development-cycle.md`: full idea-to-release workflow.
- `docs/sprints/templates/sprint.md`: sprint file template.
- `PRODUCT.md`: product vision the sprint goal must align with.
