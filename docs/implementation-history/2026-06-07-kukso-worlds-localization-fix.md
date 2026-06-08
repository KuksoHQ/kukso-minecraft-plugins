# Kukso Worlds Localization Fix
Date: 2026-06-07

## Plan
Fix `kw list` returning the `KuksoLib API not available` fallback instead of translated `commands.list.*` messages, then fix unresolved `<name>`/`<status>` placeholders after translations began loading.

## Implementation
Registered the static `KuksoAPIProvider` during KuksoLib service startup and cleared it on shutdown. Added `Lang.loadAdditional(Plugin)` so dependent plugins can merge their own `lang/*.yml` files into the shared translation map without wiping KuksoLib translations. KuksoWorlds now saves and merge-loads its language files after KuksoLib is available. Also corrected two console-only `Lang.t` calls in KuksoWorlds command dispatch where key and locale arguments were reversed.

Updated `PlaceholderManager` to resolve both `{key}` and `<key>` token styles. KuksoLib messages already use `{key}`, while KuksoWorlds language files use `<key>`.

## Verification
Ran `./gradlew.bat build`; build passed after both fixes.

## Notes
Deploy updated KuksoLib and KuksoWorlds jars together. KuksoWorlds depends on KuksoLib and now expects the provider registration, merged language-loading behavior, and `<key>` placeholder support from the updated Lib module.
