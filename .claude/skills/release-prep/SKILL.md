---
name: release-prep
description: "Prepare a per-module release: verify sprint coverage, finalize the changelog section, bump version.txt, validate, and hand off to the manual GitHub Release workflow."
---

# Release Prep

Prepare one module (`lib`, `dialogs`, `dialogs-exp-config-addon`, `worlds`, or `items`) for release. Releases are per module: one version, one tag, one GitHub Release, one jar. Read `docs/releases/README.md` for full rules if anything below is ambiguous.

## Instructions

1. Confirm with the user which module and target version to release. Versions are SemVer `X.Y.Z`; prereleases use `X.Y.Z-alpha.N`, `X.Y.Z-beta.N`, or `X.Y.Z-rc.N` (never compact forms like `2.0.0-beta1`).
2. Run `scripts/check-release-ready.sh <module> <version>` from this skill's directory. Fix every reported problem before continuing.
3. Confirm all release-relevant work for this module is represented in `docs/sprints/` and listed under `## [Unreleased]` in `CHANGELOG.md`.
4. In `CHANGELOG.md`, move this module's entries from `Unreleased` into a new release section using the Release Template at the bottom of the file: `## [kukso-minecraft-<module-slug>-v<version>] - YYYY-MM-DD`, with Sprint Links to the sprint docs that produced the work.
5. Update `modules/<module>/version.txt` to the target version. Do this only in this release prep change, never in ordinary feature or fix PRs.
6. Validate: run `./gradlew :<module>:build` (or `./gradlew build` for coordinated changes) and `git diff --check`.
7. Re-run `scripts/check-release-ready.sh <module> <version>` to confirm readiness.
8. Open the release prep PR (Conventional Commit `release(<module>): prepare v<version>`), following `.github/pull_request_template.md`. This is a human-in-the-loop checkpoint: do not tag or publish anything yourself.
9. After the PR merges to `main`, the user runs the manual GitHub `Release` workflow with inputs `module` and `version`. The workflow validates `version.txt`, creates the tag `kukso-minecraft-<module-slug>-v<version>`, builds the module, and publishes only that module's jar. For coordinated multi-module releases, run it once per module.

## Tools

- `scripts/check-release-ready.sh <module> <version>`: deterministic readiness check (module exists, version format, version.txt state, changelog section).

## References

- `docs/releases/README.md`: versioning, tag, and publishing rules.
- `docs/development-cycle.md` (steps 5-9): where release prep fits in the full cycle.
