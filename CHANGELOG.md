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
- `repo`: Added Claude Code project skills (`/sprint`, `/release-prep`) that encode the sprint and release workflows, a deterministic release-readiness check script, and a root `CLAUDE.md` importing `AGENTS.md`.
- `repo`: Added a root `LICENSE` (moved from `modules/lib`; later switched to All Rights Reserved, see Changed).
- `repo`: `LICENSE` is now bundled into all module jars as `META-INF/LICENSE`.
- `repo`: Added a root `.gitattributes` for consistent line-ending and text/binary handling.

### Changed

- `worlds`: Removed empty unused stubs (`ReloadCmd`, `PvpHandling`, `modules.WorldBorder`, and unused Color/Version/Localization hook classes) and collapsed `CmdRegistrar` to a single registration loop for the live commands.
- `repo`: Switched the repository license from Apache 2.0 to All Rights Reserved; the root `LICENSE`, the `:lib` Maven POM, and module README license statements now match.
- `lib`: Rewrote the module README to match implemented behavior: documented the ProtocolLib requirement, corrected the consumer list (`:worlds` requires, `:items` soft-depends, `:dialogs` not yet), synced usage/API examples, and moved unimplemented features (REST API, GUI API, auto-updater) fully into the backlog.
- `lib`: Removed `plugin.yml` softdepends on unreleased plugins (KuksoLoots, KuksoSense, KuksoConquest, KuksoDungeons); they return when each plugin exists.
- `docs`: `PRODUCT.md` now records the `:lib` adoption direction, a Shipped/Planned/Not-planned feature-status convention, documentation audience rules, and the repository license.
- `docs`: Rewrote all module READMEs around shipped behavior only (purpose, features/utilities, install, usage, API examples where available, terms, and support) and moved TODO/future-work notes into `docs/sprints/backlog.md`.
- `lib`, `items`: Removed future-facing placeholder notes from default resource files; the follow-up work now lives in the backlog.
- `docs`: Established `CHANGELOG.md` as the canonical release-to-sprint index.
- `docs`: Added runtime-reloadable configuration as a cross-plugin product principle, with explicit restart-required exceptions when needed.
- `docs`: Migrated the 2026-06-07 KuksoWorlds localization implementation note into the sprint workflow at `docs/sprints/2026-06-07-sprint-1.md`.
- `docs`: Standardized the PR and commit exit workflow around required PR checks and Conventional scoped commit messages.
- `repo`: Relocated project skills to `.agents/skills`, with `.claude/skills` as a symlink for Claude Code discovery.
- `lib`: The Maven POM now declares the All Rights Reserved license.
- `repo`: Removed redundant per-module `.gitignore` files and empty `gradle.properties` files; local run and credential ignore rules now live in the root `.gitignore`.

### Fixed

- `ci`: Removed a dead nested KuksoLib publish workflow (`modules/lib/.github/workflows/publish.yml`) that echoed a secret-derived value and contradicted the per-module release process, and hardened `.gitignore` against committable secret files.
- `items`: Fixed `plugin.yml` load order (`load: POSTWORLD`) and added a `description`.
- All modules: standardized the `plugin.yml` `website` to `https://kukso.com`.
- `items`: Fixed the README license statement to match the repository's All Rights Reserved `LICENSE`.
- All modules: Repaired double-encoded UTF-8 (mojibake) introduced during the monorepo migration across Java string literals, comments, language files (`lang/tr.yml`), `items.yml`, and module READMEs; player-facing messages and Turkish localization render correctly again.
- `dialogs`: Aligned command registration, user-facing usage strings, default dialog permissions, and bundled examples with the shipped `/kuksodialogs` namespace.
- `dialogs`: Corrected README and bundled confirmation examples to use list-shaped inputs, recognized confirmation button keys, and the supported `custom` action type.
- `dialogs`: Aligned the bundled feedback form custom action key with the README API example.
- `worlds`: Removed the unregistered `/kuksoworlds reload` path from the README installation instructions.

### Config And Permissions

- `worlds`: Removed default config for the unregistered `reload` subcommand and unused `plugin.yml` nodes `kuksoworlds.admin` / `kuksoworlds.mod` / `kuksoworlds.user`.
- `lib`: Removed the dead `restful-enabled` default, REST status logging, and unregistered `log`/`gui` command config blocks.
- `dialogs`: Replaced legacy command permissions with `kuksodialogs.admin.*`, added default config for the registered `addons` subcommand, and removed defaults for unregistered `help`/`forceclose` subcommands.
- `worlds`: Removed unimplemented module toggles from the default config and README example; `modules.custom-logger` remains because it is used by the KuksoLib-backed logger hook.
- `items`: Removed the unused plugin-wide `debug` default from `config.yml`.

### Sprint Links

- Sprint not assigned yet.
- `docs/sprints/2026-09-06-sprint-5.md`
- `docs/sprints/2026-06-07-sprint-1.md`
- `docs/sprints/2026-06-30-sprint-1.md`
- `docs/sprints/2026-07-01-sprint-2.md`
- `docs/sprints/2026-07-03-sprint-3.md`

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
