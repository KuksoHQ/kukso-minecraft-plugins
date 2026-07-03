# Kukso Minecraft Product Vision

Kukso Minecraft is a small, interconnected family of Paper/Spigot plugins for server owners who want practical server features without heavy operational overhead. The plugins should feel native to Minecraft administration: predictable commands, readable YAML, clear permissions, localized messages, and safe defaults.

## Product Promise

Kukso plugins help server owners add common server features quickly while keeping behavior understandable and maintainable. Each plugin should solve a focused server problem, and the monorepo should make those plugins feel like one consistent toolkit.

## Primary Users

- **Server owners and admins:** Need plugins that install cleanly, expose obvious configuration, and fail with actionable messages.
- **Builders and content teams:** Need world, dialog, and item tools that are configurable without code changes.
- **Plugin developers:** Need shared services from `:lib` that reduce duplicate code and keep integration points stable.
- **Players:** Should experience fast, localized, polished interactions without needing to understand the plugin stack.

## Product Feel

The plugins should feel:

- **Minecraft-native:** Commands, permissions, messages, and YAML should match familiar server-admin patterns.
- **Low-friction:** Download, drop into `/plugins`, configure, and run should remain the default path.
- **Predictable:** Avoid hidden behavior, surprising config mutations, or complex setup flows.
- **Consistent:** Shared command style, localization behavior, placeholders, logging, and permission naming should be reused across modules.
- **Lightweight:** Prefer focused features over broad platform abstractions unless a shared abstraction clearly reduces module complexity.
- **Admin-friendly at runtime:** Config changes should reload through a simple plugin reload command when practical; restart-required changes are acceptable when runtime reload would be unsafe or disproportionately complex.
- **Safe:** Destructive actions should require clear intent, and defaults should avoid data loss or server instability.
- **Extensible where it matters:** Addons and APIs should be stable enough for integrations without making every plugin feel like a framework.

## Module Roles

- `:lib` is the shared foundation for localization, placeholders, logging, hooks, commands, and cross-plugin services.
  - **Direction:** every Kukso module will adopt `:lib`. The near-term focus is making `:lib` stable and clear about its core use cases — centralized logging, YAML localization, color/text formatting, and placeholders — before broadening adoption.
  - **Current adoption:** `:worlds` requires it, `:items` soft-depends on it, `:dialogs` does not use it yet.
- `:dialogs` provides configurable player-facing dialog flows, screens, validators, and commands.
- `:dialogs-exp-config-addon` proves that dialog functionality can be extended through a focused service-loaded addon.
- `:worlds` provides lightweight world management with clear commands and config-driven world behavior.
- `:items` provides YAML-defined custom items, identification utilities, and optional localized lore behavior.

## Cross-Plugin Principles

- Prefer one clear config shape over multiple equivalent ways to do the same thing.
- Keep commands discoverable, permissioned, and consistent across plugins.
- Use localization and placeholders as first-class behavior, not as an afterthought.
- Keep runtime logs useful for server admins: concise by default, detailed when debug mode is enabled.
- Make config entries runtime-reloadable through a simple `/... reload` command whenever practical.
- Document or message config entries that require a server restart or full plugin lifecycle reset.
- Reuse `:lib` for shared patterns before duplicating behavior in feature plugins.
- Keep public APIs and service-loaded extension points small, documented, and stable.
- Validate configs early and report errors in language a server admin can act on.

## Feature Status Convention

Every feature is in exactly one state, and documentation must not blur them:

- **Shipped:** implemented and verified; may appear in READMEs as available behavior.
- **Planned:** wanted but not implemented; must have a backlog item in `docs/sprints/backlog.md`, and READMEs may only mention it under an explicit "planned" heading. Config flags reserved for planned features (such as `restful-enabled`) must be documented as reserved.
- **Not planned:** out of scope; remove stale references instead of keeping them.

Currently planned: `:lib` REST API, `:lib` GUI API, `:lib` auto-updater, and the future plugins KuksoLoots, KuksoSense, KuksoConquest, and KuksoDungeons (unstarted; their `:lib` softdepend entries return when each plugin exists).

## Documentation Audience

- Module `README.md` files are public-facing, written for server owners and plugin users. They must reflect shipped behavior only (plus a clearly marked planned section).
- Design notes, scratch ideas, and implementation details belong in `docs/sprints/backlog.md` or sprint docs, never in module READMEs.
- `PRODUCT.md` (this file) is the source of intent: when code and docs disagree, record the ruling here so future sessions do not have to rediscover it.

## License

The repository is proprietary, **All Rights Reserved** (see the root `LICENSE`). Module READMEs and published artifact metadata must state this license and no other.

## Scope Boundaries

Kukso Minecraft is not trying to become a large server platform, economy suite, or all-in-one gameplay system. New work should belong because it strengthens the plugin family, improves shared foundations, or solves a focused operational need for Minecraft servers.

## Release Philosophy

Releases should be understandable to server operators. Each release should explain what changed, which module is affected, whether config or permissions changed, and which sprint docs produced the shipped work.
