# Development Cycle

This repository uses a lightweight, traceable development cycle. The goal is not ceremony; the goal is making every meaningful idea, implementation, validation step, PR, and release easy to follow later.

## 1. Capture The Idea

Drop new feature ideas, bugs, refactors, or documentation work into `docs/sprints/backlog.md`.

- Use `Candidate` for fresh ideas that are not fully shaped.
- Use `Ready` when an item has enough user value, acceptance criteria, and validation detail to implement.
- Use `Blocked` when an item depends on a decision or external input.
- Use `Done` for completed backlog records.

Example:

```md
- [ ] **Module/area:** `:worlds`
  - **User value:** Admins can clone an existing world configuration instead of recreating it manually.
  - **Acceptance criteria:** `/kuksoworlds clone <source> <target>` copies supported world settings and reports clear success/failure messages.
  - **Validation:** `./gradlew :worlds:build` and manual command check in a Paper server.
```

## 2. Commit Work To A Sprint

Before implementing non-trivial work, create or update the current sprint file under `docs/sprints/`.

Use the sprint template:

```text
docs/sprints/templates/sprint.md
```

Sprint files use:

```text
docs/sprints/YYYY-MM-DD-sprint-N.md
```

Move selected backlog items into the sprint as committed items. Each item should name the module or area, user value, release target, changelog impact, acceptance criteria, and validation expectations.

## 3. Implement And Log Meaningful Progress

Make the code or documentation changes in the relevant module or repo area.

Update the sprint work log when something meaningful happens:

- Item status changes.
- A blocker appears.
- Scope changes.
- A decision matters for future maintainers or agents.
- Validation passes or fails.

Do not log every small edit.

## 4. Validate

Run the smallest relevant validation command.

Examples:

```sh
./gradlew :worlds:build
./gradlew :lib:build
./gradlew build
git diff --check
```

For plugin behavior, prefer a focused Gradle build first, then verify manually in a Paper server when the change affects commands, gameplay, dialogs, or visible server behavior.

Record the validation result in the sprint item and PR.

## 5. Update Release Records

If the work is user-visible or release-relevant, update `CHANGELOG.md` under `Unreleased`.

Use module or area labels such as:

- `:lib`
- `:dialogs`
- `:dialogs-exp-config-addon`
- `:worlds`
- `:items`
- `docs`
- `ci`

Add or keep the sprint link under `Sprint Links`.

## 6. Open The PR

Use Conventional Commit style:

```text
type(scope): summary
```

Example:

```text
feat(worlds): add world clone command
```

Open a PR with `.github/pull_request_template.md`. The PR should include changed modules, sprint or backlog link, validation output, changelog impact, release/version impact, config or permission impact, and risks or follow-ups.

## 7. Merge To Main

After review and CI pass, merge the PR into `main`.

At this point the work is complete in the repository, but not necessarily released as a downloadable plugin artifact.

## 8. Prepare A Release

When a module is ready to ship, open a release prep PR.

In that PR:

1. Confirm release-relevant work is represented in `docs/sprints/`.
2. Move relevant `CHANGELOG.md` entries from `Unreleased` into a final release section.
3. Update the affected module's `version.txt`.
4. Run the smallest relevant module build, or `./gradlew build` for coordinated changes.
5. Merge the release prep PR to `main`.

## 9. Publish The Release

Run the manual GitHub `Release` workflow from `main`.

Inputs:

```text
module: worlds
version: 2.1.0
```

The workflow validates the selected module and version, checks `version.txt`, creates a tag like:

```text
kukso-minecraft-worlds-v2.1.0
```

Then it builds the selected module and publishes only that module's jar.

For coordinated multi-module releases, run the workflow once per module.

## Full Cycle

The complete path is:

```text
idea -> backlog -> sprint -> implementation -> validation -> changelog -> PR -> merge -> release prep -> GitHub release
```

For tiny changes, keep the process proportionate. For anything non-trivial, user-visible, or release-relevant, keep the chain traceable.
