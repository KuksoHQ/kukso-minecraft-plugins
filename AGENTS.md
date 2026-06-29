# Repository Guidelines

## Project Structure & Module Organization

This is a Gradle Kotlin DSL monorepo for Kukso Minecraft Paperspigot plugins. The root `settings.gradle.kts` maps modules from `modules/`:

- `modules/lib` -> `:lib`, shared KuksoLib services, hooks, commands, localization, and resources.
- `modules/dialogs` -> `:dialogs`, dialog API, commands, builders, validators, and dialog YAML files.
- `modules/dialogs-exp-config-addon` -> `:dialogs-exp-config-addon`, a service-loaded addon for KuksoDialogs.
- `modules/worlds` -> `:worlds`, world commands, generators, hooks, and lang files.
- `modules/items` -> `:items`, item commands, utilities, and config resources.

Java source lives in `src/main/java`; plugin descriptors, config files, language files, and templates live in `src/main/resources`. Shared Gradle conventions are in `build-logic/src/main/kotlin/com/kukso/gradle/MinecraftPlugins.kt`. Product vision belongs in `PRODUCT.md`. Release history belongs in `CHANGELOG.md`. Sprint planning and delivery notes belong under `docs/sprints/`. `docs/implementation-history/` is legacy reference only.

## Product And Release Sources

- Treat `PRODUCT.md` as the repository-level product vision source: who the plugins serve, how they should feel, cross-plugin principles, module roles, and scope boundaries.
- Do not create or depend on a root `PLAN.md` unless the user explicitly requests it; use `PRODUCT.md`, `docs/sprints/`, and `CHANGELOG.md` instead.
- Treat `CHANGELOG.md` as the canonical release record and release-to-sprint index.
- Update `CHANGELOG.md` for release-relevant or user-visible changes, grouped by module or repository area when helpful.
- Link released changelog entries back to the sprint docs that produced them. Sprint docs may note intended release targets, but the changelog owns final release history.

## Agile/Scrum Workflow

Use `docs/sprints/` as the canonical planning and delivery record for future work. The repository uses lightweight Scrum with 1-week monorepo-wide sprints by default; individual sprint items should identify the affected module or repository area.

- Inspect the current sprint document and `docs/sprints/backlog.md` before planning or implementing non-trivial work.
- Tie each non-trivial code or documentation task to a sprint item before implementation. If the work is not represented yet, add or update a concise backlog or sprint item first.
- Use real sprint files named `docs/sprints/YYYY-MM-DD-sprint-N.md`; use `docs/sprints/templates/sprint.md` as the starting template.
- Keep sprint items implementation-ready but lightweight: include module or area, user value, acceptance criteria, and validation expectations.
- Record intended release targets or changelog impact on sprint items when the work is release-relevant.
- During implementation, record meaningful progress, blockers, scope changes, decisions, and verification results in the sprint work log.
- At completion, update item status, review notes, validation output, shipped artifacts, and carryover as appropriate.
- Do not add new entries under `docs/implementation-history/` unless the user explicitly requests legacy implementation-history documentation.

## Build, Test, and Development Commands

- `./gradlew projects` lists included Gradle projects.
- `./gradlew printModules` prints module paths and directories.
- `./gradlew build` compiles all modules and creates jars under each module's `build/libs/`.
- `./gradlew :worlds:build` builds one module; replace `worlds` with `lib`, `dialogs`, `items`, or `dialogs-exp-config-addon`.
- `./gradlew :worlds:runServer` starts a local Paper test server for modules using the `xyz.jpenilla.run-paper` plugin.
- `./gradlew test` runs Gradle test tasks. The repository currently has no `src/test` trees, so add tests with new behavior.

## Coding Style & Naming Conventions

Use Java 21 and UTF-8; these are enforced by the shared Gradle convention. Follow the existing 4-space Java indentation. Keep packages lowercase under `com.kukso.minecraft.<module>`. Use `PascalCase` for classes, `lowerCamelCase` for methods and fields, and existing suffixes such as `Cmd`, `Manager`, `Registrar`, `Builder`, and `Handler`.

For configuration behavior, prefer runtime reload support through a simple plugin reload command such as `/kuksoworlds reload` or the module's existing reload command. It is acceptable for some settings to require a server restart or full plugin lifecycle reset when runtime reload would be unsafe or overly complex; document or message those exceptions clearly.

## Testing Guidelines

Place unit tests in `modules/<module>/src/test/java` and name test classes `*Test`. For plugin behavior, prefer focused unit tests for parsing, validation, and command utilities, then verify in a Paper server with `:module:runServer`. Always run the smallest relevant module build before opening a PR.

## Commit & Pull Request Guidelines

Recent history uses short imperative messages, sometimes with Conventional Commit prefixes. Good examples are `Add KuksoDialogs ExpConfig addon module`, `Make Gradle wrapper executable`, and `feat: Implemented localization method of KuksoLib`.

PRs should include a short summary, changed modules, build/test output, linked issues if any, and notes for config or permission changes. Add screenshots or server logs for visible gameplay, command, or dialog behavior.

## Security & Configuration Tips

Do not commit `.gradle/`, `build/`, `run/`, IDE files, logs, or generated server state. Publishing `:lib` uses `KUKSO_USER` and `KUKSO_TOKEN` or matching Gradle properties; keep those credentials local.
