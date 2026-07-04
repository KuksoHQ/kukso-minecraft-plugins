# KuksoLib

KuksoLib is the shared foundation plugin for the Kukso Minecraft plugin family. It centralizes reusable services that feature plugins should not reimplement: translated messages, placeholder replacement, color formatting, logging, command helpers, plugin hooks, and a small Bukkit service API.

## Features And Utilities

- YAML localization with nested keys, player-locale detection, fallback language handling, and dependent-plugin language merging.
- Placeholder replacement for static config values and runtime values such as `{player}` and `{world}`; both `{key}` and `<key>` token styles are supported.
- Optional PlaceholderAPI expansion when PlaceholderAPI is installed.
- Color formatting for legacy `&` codes, `#rrggbb` hex colors, and `<gradient:#start:#end>text</gradient>` tags.
- Shared async logging with configurable file logging, debug output, warning/severe console output, and player-event logging utilities.
- `KuksoAPI` service registration through Bukkit's `ServicesManager` and `KuksoAPIProvider`.
- `/kukso` admin command utilities for help, reload, language-key lookup, version checks, and logger diagnostics.
- ProtocolLib packet translation for `translate.<key>` chat/system-chat markers.
- Optional PlaceholderAPI integration and RealisticSeasons detection.

## Installation

1. Install ProtocolLib.
2. Drop `KuksoLib-Paper-<version>.jar` into the server `plugins/` folder.
3. Start the server once so `plugins/KuksoLib/config.yml` and `plugins/KuksoLib/lang/` are generated.
4. Edit `config.yml` as needed, then use `/kukso reload` for reloadable config changes. Restart the server after language-file changes.

KuksoWorlds requires KuksoLib. KuksoItems soft-depends on it. KuksoDialogs does not currently use KuksoLib.

## Usage Examples

```yaml
server-name: kukso.net
fallback-language: en
logging-enabled: false
debug-mode: false

supported_langs:
  - "en"
  - "tr"

prefix: "#ffc13b[KuksoLib] &r"
discord: "https://discord.gg/Hqq3CdnenN"
```

```yaml
kuksolib:
  welcome: "{prefix}Welcome, {player}! You are in {world}."
```

## Example API Usage

```java
KuksoAPI api = Bukkit.getServicesManager().load(KuksoAPI.class);
api.send(player, "kuksolib.welcome", Map.of("world", "Wilderness"));
```

`{prefix}` comes from config or language placeholders, `{player}` is filled from the player context, and `{world}` comes from the call.

## Terms And Conditions

This plugin is proprietary and All Rights Reserved. See the repository-root [LICENSE](../../LICENSE). You may not copy, redistribute, modify, adapt, remove notices from, or claim ownership of the software unless Kukso grants written permission.

## Support & Contributions

Found an issue or want to request a change? Reach out through the Kukso support channels or open a request on the GitHub repository.
