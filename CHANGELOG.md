# Changelog

All notable release-relevant changes to Kukso Minecraft plugins are tracked here.

This repository releases multiple plugin artifacts from one monorepo. Release entries should identify the affected module or repository area and link to the sprint docs that produced the shipped work.

## Release Tracking Rules

- Use `## [Unreleased]` for release-relevant work that has not shipped yet.
- Use module or area labels such as `:lib`, `:dialogs`, `:worlds`, `:items`, `build-logic`, or `docs`.
- Link final release entries to the sprint docs that contributed to the release.
- Keep sprint work logs in `docs/sprints/`; keep final release history here.
- Use per-module release tags in the format `kukso-minecraft-<module-slug>-v<version>`, such as `kukso-minecraft-worlds-v2.0.0`.
- Use `docs/releases/` for the release prep and publishing checklist.

## [Unreleased]

### Added

- `docs`: Added product vision and release-tracking documentation.
- `docs`: Added release workflow documentation for per-module SemVer, release prep PRs, tags, and GitHub releases.
- `docs`: Added a full development cycle guide covering idea capture, sprint execution, PRs, release prep, and GitHub releases.
- `ci`: Added a manual per-module release workflow that validates module versions and publishes only the selected module artifact.

### Changed

- `docs`: Established `CHANGELOG.md` as the canonical release-to-sprint index.
- `docs`: Added runtime-reloadable configuration as a cross-plugin product principle, with explicit restart-required exceptions when needed.
- `docs`: Migrated the 2026-06-07 KuksoWorlds localization implementation note into the sprint workflow at `docs/sprints/2026-06-07-sprint-1.md`.
- `docs`: Standardized the PR and commit exit workflow around required PR checks and Conventional scoped commit messages.

### Sprint Links

- Sprint not assigned yet.
- `docs/sprints/2026-06-07-sprint-1.md`
- `docs/sprints/2026-06-30-sprint-1.md`

## Release Template

Copy this section when publishing a release.

```md
## [kukso-minecraft-module-slug-vX.Y.Z] - YYYY-MM-DD

### Added

- `:module`: User-visible addition.

### Changed

- `:module`: User-visible change.

### Fixed

- `:module`: User-visible fix.

### Config And Permissions

- `:module`: Config, permission, migration, or compatibility notes.

### Sprint Links

- `docs/sprints/YYYY-MM-DD-sprint-N.md`
```
