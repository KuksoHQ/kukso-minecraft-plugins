# Changelog

All notable release-relevant changes to Kukso Minecraft plugins are tracked here.

This repository releases multiple plugin artifacts from one monorepo. Release entries should identify the affected module or repository area and link to the sprint docs that produced the shipped work.

## Release Tracking Rules

- Use `## [Unreleased]` for release-relevant work that has not shipped yet.
- Use module or area labels such as `:lib`, `:dialogs`, `:worlds`, `:items`, `build-logic`, or `docs`.
- Link final release entries to the sprint docs that contributed to the release.
- Keep sprint work logs in `docs/sprints/`; keep final release history here.
- Use the repository tag format documented in `README.md`, such as `kukso-minecraft-worlds-v1.2.0`.

## [Unreleased]

### Added

- `docs`: Added product vision and release-tracking documentation.

### Changed

- `docs`: Established `CHANGELOG.md` as the canonical release-to-sprint index.
- `docs`: Added runtime-reloadable configuration as a cross-plugin product principle, with explicit restart-required exceptions when needed.

### Sprint Links

- Sprint not assigned yet.

## Release Template

Copy this section when publishing a release.

```md
## [kukso-minecraft-module-vX.Y.Z] - YYYY-MM-DD

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
