# Product Backlog

Use this file for ongoing product and maintenance backlog items. Keep entries short enough to groom quickly, but specific enough that an agent can implement them without rediscovering the goal.

## Ready

- _No ready items._

## Candidate

- [ ] **Module/area:** `:lib`, all plugin modules
  - **User value:** Turkish players see command output in Turkish when their client locale is Turkish instead of falling back to English for commands.
  - **Acceptance criteria:** Diagnose whether player locale detection, language-key lookup, command sender handling, or missing module lang keys cause command output to stay English; update the shared localization path and affected module command messages so Turkish client locale receives Turkish output where translations exist, with clear fallback behavior for missing keys.
  - **Validation:** Unit tests for the localization/fallback path where feasible, plus manual Paper server checks with a Turkish client locale for `/kukso`, `/kuksodialogs`, `/kuksoworlds`, and other affected command outputs.

- [ ] **Module/area:** `:lib`
  - **User value:** Admins can run `/kukso version` without a command exception and receive a useful version/update response.
  - **Acceptance criteria:** Reproduce and fix the `/kukso version` exception, including alias/subcommand handling if `/kukso ver` and `/kukso version` are both intended to work; return a localized error or usage message for any unsupported version-check path instead of throwing.
  - **Validation:** `./gradlew :lib:test` or the smallest relevant test task, plus a manual Paper server check for `/kukso version` and `/kukso ver`.

- [ ] **Module/area:** `:dialogs`, `:items`
  - **User value:** Non-English players get localized dialog and item text consistent with `:lib` and `:worlds`.
  - **Acceptance criteria:** Add `lang/` files (at minimum `en`, `tr`) for the `:dialogs` and `:items` modules, matching the existing `lang/` structure and key conventions used in `:lib` and `:worlds`.
  - **Validation:** Manual Paper server check with a non-English locale, plus a review that lang keys match usages in code.

- [ ] **Module/area:** `ci`
  - **User value:** Style and formatting regressions are caught automatically instead of relying on manual review.
  - **Acceptance criteria:** Add lint/format checks to `.github/workflows/build.yml` (e.g. a Java formatter/checkstyle step) that fail the workflow on violations.
  - **Validation:** Push a deliberately misformatted change to a branch and confirm the workflow fails; confirm it passes on clean code.

- [ ] **Module/area:** `:dialogs`, `:dialogs-exp-config-addon`
  - **User value:** Simpler, less error-prone build wiring between the dialogs module and its example addon.
  - **Acceptance criteria:** Evaluate replacing the embedded exp-config addon jar (currently bundled into `:dialogs` as a resource) with a proper Gradle project dependency or an explicit copy task, and document the tradeoffs before implementing.
  - **Validation:** N/A until scoped; this item is evaluation-only until a decision is made.

- [ ] **Module/area:** `:dialogs`
  - **User value:** Server links can be updated through the normal reload workflow instead of forcing a full server restart.
  - **Acceptance criteria:** `/kuksodialogs reload` reloads server link config or clearly separates a dedicated reload command; command output and README wording match the implemented behavior.
  - **Validation:** `./gradlew :dialogs:build` and manual Paper server check that changed links are visible after reload.

- [ ] **Module/area:** `:dialogs`
  - **User value:** Server owners get polished preset dialog workflows only after they are implemented and documented as shipped behavior.
  - **Acceptance criteria:** Scope and implement preset dialogs for terms/privacy, patch notes, teleport menu, favorite commands, confirmation examples, feedback/bug reports, and register/login only when each flow has real YAML, permissions, actions, and validation.
  - **Validation:** `./gradlew :dialogs:build`, `/kuksodialogs validate`, and manual open/action checks for each shipped preset.

- [ ] **Module/area:** `:dialogs`
  - **User value:** KuksoDialogs follows the same shared logging and localization behavior as the rest of the plugin family.
  - **Acceptance criteria:** Integrate KuksoLib logging and localization intentionally, including dependency/soft-dependency choice, lang files, fallback behavior, command messages, and README updates once shipped.
  - **Validation:** `./gradlew :dialogs:build` and manual Paper server checks with English and Turkish client locales.

- [ ] **Module/area:** `:lib`
  - **User value:** Server owners can query server state over HTTP once the feature has a real implementation instead of a dead default config key.
  - **Acceptance criteria:** Design and implement a default-off REST API with documented endpoint surface, port/bind config, and auth behavior; add the config keys in the same change that implements them; document it in the lib README once shipped.
  - **Validation:** Unit tests for request handling plus a manual Paper server check with the REST API enabled and disabled.

- [ ] **Module/area:** `:lib`
  - **User value:** Plugin developers get a shared GUI API (paged layouts, borders, close/back buttons) so feature plugins stop hand-rolling inventories.
  - **Acceptance criteria:** Implement the GUI module that `KuksoAPI.openTestGui` anticipates, adding real config only when behavior exists. Preserved design notes: 6-line GUIs use a border in slots [0-9, 17, 18, 26, 27, 35, 36, 44-48, 50-53]; 6-line multi-page uses slots 48/49/50 for Previous/Close/Next; 6-line single-page uses slot 49 for Close; 3-line GUIs may toggle border and use slots 21/22/23 for buttons; 1-line GUIs have no border and 9 custom slots; 2-, 4-, and 5-line layouts are invalid and should error.
  - **Validation:** Unit tests for layout/slot math; manual Paper server check of border, paging, and close behavior.

- [ ] **Module/area:** `:lib`
  - **User value:** Server owners get the auto-updater the README used to promise: opt-in automatic download of new KuksoLib releases instead of only the manual `/kukso ver` check.
  - **Acceptance criteria:** Add an opt-in auto-update config flag (default off) that reuses the GitHub release lookup in `VersionChecker`, downloads the new jar safely, and messages admins about applied/pending updates.
  - **Validation:** Unit tests for version comparison and download-path handling; manual Paper server check of the update flow.

- [ ] **Module/area:** `:lib`
  - **User value:** `/kukso ver` no longer risks stalling the main server thread on a slow GitHub API response.
  - **Acceptance criteria:** Make `VersionChecker`'s HTTP fetch asynchronous (e.g. Bukkit async scheduler or `CompletableFuture`), delivering the result back on the main thread for messaging.
  - **Validation:** Unit test for the comparison logic; manual `/kukso ver` check confirming no server stall with simulated latency.

- [ ] **Module/area:** `:lib`
  - **User value:** Admins can update language files without a full server restart when using the documented reload workflow.
  - **Acceptance criteria:** `/kukso reload` reloads KuksoLib language files into `Lang` as well as config, and dependent plugins that merge language files have a clear reload path or documented restart requirement.
  - **Validation:** `./gradlew :lib:test` for language reload behavior plus manual Paper server check with an edited language key.

- [ ] **Module/area:** New plugins (KuksoLoots, KuksoSense, KuksoConquest, KuksoDungeons)
  - **User value:** Planned feature plugins that will build on KuksoLib once it is stable.
  - **Acceptance criteria:** Not yet scoped; these are placeholders so the plan is not lost. When each plugin is started, add it as a module, and re-add its softdepend entry to `modules/lib/src/main/resources/plugin.yml` (removed 2026-07-04 because the plugins do not exist yet).
  - **Validation:** N/A until scoped.

- [ ] **Module/area:** `:lib`
  - **User value:** Regressions in the localization core (the lib's main value) are caught by `./gradlew test`.
  - **Acceptance criteria:** Add focused unit tests for `Lang`, `LocaleLoader`, and `PlaceholderManager` (key lookup, fallback-language behavior, placeholder substitution); only `ColorManagerTest` covers `:lib` today.
  - **Validation:** `./gradlew :lib:test` green with the new tests executed.

- [ ] **Module/area:** `:worlds`
  - **User value:** PlaceholderAPI users get a real chunk-unlock count instead of a constant placeholder value.
  - **Acceptance criteria:** Define the chunk-tracking source of truth, wire `%kuksoworlds_chunks_unlocked%` to that source, and document the placeholder only once the value is meaningful.
  - **Validation:** Unit test the count source if feasible, plus manual PlaceholderAPI check in a Paper server.

- [ ] **Module/area:** `:worlds`
  - **User value:** Admins can apply supported KuksoWorlds config changes without a full server restart.
  - **Acceptance criteria:** Implement and register `/kuksoworlds reload`; reload safe runtime state from `config.yml`; clearly message any settings that still require restart or full world lifecycle handling; add command config, permissions, language keys, and README usage only once the command works.
  - **Validation:** `./gradlew :worlds:build` plus manual Paper server check that a supported config change takes effect after `/kuksoworlds reload` and restart-required changes are reported clearly.

- [ ] **Module/area:** `:items`
  - **User value:** Server owners get real localized or client-side item lore only when the behavior is implemented, verified, and documented accurately.
  - **Acceptance criteria:** Decide whether localized lore belongs in KuksoItems, KuksoLib, or their integration; implement the chosen path; support documented `items.yml` shape; update the README only after the behavior works.
  - **Validation:** `./gradlew :items:build` plus manual Paper server check with at least two locales if localization ships.

- [ ] **Module/area:** `:items`
  - **User value:** Item metadata such as rarity has clear runtime behavior instead of being a placeholder in `items.yml`.
  - **Acceptance criteria:** Define supported metadata keys, how they are stored in NBT or item meta, and how other plugins should read them; remove or implement placeholder config comments.
  - **Validation:** Unit tests for metadata parsing plus `./gradlew :items:build`.

- [ ] **Module/area:** `:votes` (P0 — MVP reward plugin) — *scope sharpened by the [switching research](../research/2026-07-12-votingplugin-switching-research.md)*
  - **User value:** New/greenfield servers and owners stuck on the abandoned NuVotifier two-plugin stack get a single-jar, vote-site-agnostic plugin that receives votes and grants rewards with a *testable* 5-minute setup. It is not another VotingPlugin clone and not a feature-parity race. Stealing installed VotingPlugin setups is a later, higher-friction segment — do NOT target it first (switching inertia is high; owners tolerate VP's setup pain for its power).
  - **Acceptance criteria:**
    - Votifier v1 (RSA) + modern token/HMAC receiver in one jar (no separate Votifier/NuVotifier required) — attacks the two-plugin tax, which is the real open surface (NuVotifier's last release was 2021).
    - **Testable 5-minute happy path:** sensible/auto port binding with clear diagnostics; print the public key/token once, clearly; a `/kuksovotes test` command; a "last inbound vote received" status/log; and ONE default reward that fires with zero YAML edits.
    - **Service-site footgun guard:** on an unknown service name, log the exact received string plus a copy-paste snippet to register that site (auto-create where possible). This is the #1 real-world failure mode.
    - Multiple vote sites; configurable rewards (console commands, items, Vault money via soft-dep); offline vote queue delivered on join; broadcast + per-player messages; `/vote` site listing; `/kuksovotes reload`; basic PlaceholderAPI placeholders.
    - **Exactly one** retention hook (choose VoteParty *or* vote points/shop *or* simple streaks) so it is not rejected as "too simple" after day one — not all three.
  - **Explicitly out of P0 (evidence says overvalued):** deep GUI/editor parity, full AdvancedCore reward depth, multi-proxy/MySQL perfection, and leaning on the FirstSpawn "verified badge" as an acquisition driver (no public evidence owners choose vote plugins for platform badges).
  - **Validation:** Unit tests for protocol decode/validation and reward/service-name parsing under `modules/votes/src/test/java`; manual Paper `:votes:runServer` driving a simulated vote. **Kill metric:** measured time-to-first-successful-vote in a clean Paper install must be ≤ ~10 minutes with defaults.
  - **Distribution (treat as product work, not an afterthought):** Spigot resource listing + a host-style setup guide + a short 5-minute setup video. Without distribution the UX advantage is invisible — cf. VoteRewards, which markets this exact thesis and has ~37 downloads.

- [ ] **Module/area:** `:votes` (P1 — FirstSpawn integration)
  - **User value:** Owners can opt in to share server-level activity with FirstSpawn to earn a free verified badge, and players can vote for the server through FirstSpawn like any other vote site.
  - **Acceptance criteria:** Add FirstSpawn as a first-class vote site; add an opt-in, owner-controlled telemetry emitter that sends only server-level aggregate signals; surface exactly what is shared and allow disabling it at runtime; implement the verified-badge handshake. No player-level identity data is collected. Honors the KuksoVotes telemetry ruling in `PRODUCT.md`.
  - **Validation:** Unit tests for the telemetry payload shape and opt-in gating; manual end-to-end check against a FirstSpawn staging endpoint with telemetry enabled and disabled.

- [ ] **Module/area:** `:votes` (P2 — engagement depth)
  - **User value:** Servers can drive more voting with streaks, server-wide vote parties, vote points/currency, and network leaderboards.
  - **Acceptance criteria:** Add vote streaks, vote-party goals and rewards, a vote-points economy, and per-period leaderboards, all config-driven and reloadable via `/kuksovotes reload`. Player-level cross-server identity remains out of scope pending a consent model.
  - **Validation:** Unit tests for streak/party/points logic; manual Paper server checks for each feature and reload behavior.

- [ ] **Module/area:** `docs`
  - **User value:** Windows contributors are not silently blocked or given a broken checkout by the `.claude/skills` symlink.
  - **Acceptance criteria:** Document that Windows contributors need `git config core.symlinks=true` (and Developer Mode or admin rights) for the `.agents/skills` -> `.claude/skills` symlink to check out correctly; add this note to the relevant contributor-facing doc.
  - **Validation:** Read-through of the added documentation note.

## Blocked

- _No blocked items yet._

## Done

- [x] **Module/area:** All plugin modules
  - **User value:** Regressions in parsing, validation, and command utilities are caught by `./gradlew test` instead of manual Paper server checks.
  - **Acceptance criteria:** Each module with testable parsing/validation/command-utility logic gets a seeded `modules/<module>/src/test/java` tree with at least one focused `*Test` class covering real behavior (no placeholder asserts), and `./gradlew test` runs them green.
  - **Validation:** Delivered in `2026-07-03-sprint-3.md`; `./gradlew test` green with 16 tests across five modules.

- [x] **Module/area:** All module READMEs, docs
  - **User value:** Server owners see module READMEs that describe shipped behavior instead of roadmap, TODO, or scratch planning notes.
  - **Acceptance criteria:** Rewrite module READMEs around purpose, shipped features/utilities, installation, usage examples, API examples where applicable, terms, and support; remove public default-resource and source-level future notes; move future work into this backlog; update `PRODUCT.md` so planned work stays out of module READMEs.
  - **Validation:** Delivered in `2026-07-03-sprint-3.md`; README/resource/source future-work grep clean, `git diff --check` green, and targeted `:lib`/`:worlds` compile green.

- [x] **Module/area:** `:dialogs`
  - **User value:** Admins can use the documented KuksoDialogs command and permission names without discovering legacy namespace remnants.
  - **Acceptance criteria:** Align `plugin.yml`, command registration, config command permissions, examples, and user-facing usage strings around `/kuksodialogs`; remove default config for unregistered subcommands.
  - **Validation:** Delivered in `2026-07-03-sprint-3.md`; legacy namespace grep over `modules/` clean, stale config-key grep clean, `git diff --check` green, and affected module builds green.

- [x] **Module/area:** Repository exit workflow
  - **User value:** Completed work exits the repo through a predictable PR, versioning, changelog, tag, artifact, and release process.
  - **Acceptance criteria:** `AGENTS.md`, `docs/releases/`, the PR template, changelog rules, sprint record, and release workflow define per-module SemVer, Conventional scoped commits, PR-first delivery, release prep PRs, and manual per-module GitHub Action releases.
  - **Validation:** Run `git diff --check -- AGENTS.md CHANGELOG.md docs/sprints docs/releases .github` and review the release workflow YAML.

- [x] **Module/area:** Repository workflow documentation
  - **User value:** Historical implementation work is traceable through the current sprint workflow instead of split across legacy implementation-history notes.
  - **Acceptance criteria:** Convert `docs/implementation-history/2026-06-07-kukso-worlds-localization-fix.md` into `docs/sprints/2026-06-07-sprint-1.md`, preserve the legacy plan, implementation, verification, and notes, and remove the old file to avoid duplicate records.
  - **Validation:** Read the converted sprint document and run `git diff --check -- docs/sprints CHANGELOG.md`.

- [x] **Module/area:** Repository product and release documentation
  - **User value:** Future work has a clear product vision source and a release record that links shipped versions back to sprint execution.
  - **Acceptance criteria:** Root `PRODUCT.md` defines product vision and plugin feel, including runtime-reloadable config expectations, root `CHANGELOG.md` tracks release-relevant changes, and `AGENTS.md` explains how product, sprint, changelog, and config-reload guidance relate.
  - **Validation:** Read the created Markdown files and confirm `git status --short` only shows intended documentation changes plus any pre-existing user changes.

- [x] **Module/area:** Repository workflow documentation
  - **User value:** Future LLM-assisted sessions follow a consistent lightweight Scrum process instead of writing ad hoc implementation-history notes.
  - **Acceptance criteria:** `AGENTS.md` points future agents to `docs/sprints/`, sprint workflow docs exist, a backlog exists, a reusable sprint template exists, and `docs/implementation-history/` is retired.
  - **Validation:** Read the created Markdown files and confirm `git status --short` only shows the intended documentation changes plus any pre-existing user changes.
