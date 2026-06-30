# Release Workflow

This repository releases multiple Paper/Spigot plugin artifacts from one monorepo. Releases are per module by default: one module version, one module tag, one GitHub Release, and one module jar.

For the complete idea-to-release flow, see `../development-cycle.md`.

## Versioning

- Each module owns its version in `modules/<module>/version.txt`.
- Stable versions use `X.Y.Z`.
- Prereleases use `X.Y.Z-alpha.N`, `X.Y.Z-beta.N`, or `X.Y.Z-rc.N`.
- Do not use compact prereleases such as `2.0.0-beta1` for new releases.
- Change `version.txt` in a release prep PR, not in ordinary feature or fix PRs.

## Commit And PR Exit

- Use Conventional Commits: `type(scope): summary`.
- Use scopes from `AGENTS.md`, such as `worlds`, `lib`, `docs`, `ci`, or `repo`.
- Deliver completed work through a PR by default.
- Every PR should reference a sprint or backlog item, list changed modules, include validation output, and state changelog, release, config, and permission impact.

## Release Prep PR

Use a release prep PR when a module is ready to ship.

1. Confirm all release-relevant work is represented in `docs/sprints/`.
2. Move relevant `CHANGELOG.md` entries from `Unreleased` into a final release section.
3. Update the affected module's `version.txt`.
4. Run the smallest relevant module build, or `./gradlew build` for coordinated changes.
5. Merge the release prep PR to `main`.

## Tags And GitHub Releases

Release tags use:

```text
kukso-minecraft-<module-slug>-v<version>
```

Examples:

```text
kukso-minecraft-worlds-v2.0.0
kukso-minecraft-lib-v2.0.1
kukso-minecraft-worlds-v2.1.0-beta.1
```

After the release prep PR is merged, run the manual `Release` GitHub Action from `main` with the module and version. The workflow validates the selected module, verifies `version.txt`, creates the tag, builds the selected module, and publishes only that module's jar.

For coordinated multi-module releases, run the release workflow once per module so each module keeps its own tag, artifact, and release notes.

## KuksoLib Publishing

When releasing `:lib`, the workflow also keeps the existing optional behavior:

- Publish KuksoLib JavaDocs to the website pages repository.
- Publish the Maven artifact only when `ENABLE_MAVEN_PUBLISH` is `true` and the required credentials are configured.
