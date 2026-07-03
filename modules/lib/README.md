[![Wiki](https://img.shields.io/badge/Wiki-Kukso_Labs-blue.svg)](https://labs.kukso.net/docs/category/common-in-all-plugins)

# KuksoLib

Modular API foundation for Kukso plugins, centralizing core functions to simplify maintenance and reduce overall plugin size.

---

## Core Features

- 🌐 **Localization:** YAML-based i18n with `{placeholders}`, per-player client-locale detection, and automatic fallback to the configured fallback language.
- 🧾 **Centralized logging and debug:** A shared logging service for all Kukso plugins — missing-key diagnostics, fallback events, optional file logging under `/plugins/KuksoLib`, and a debug mode.
- 🎨 **Color manager:** Legacy `&` codes, `#rrggbb` hex colors, and `<gradient:...>` gradients in one formatter.
- 📌 **Placeholders:** Dynamic placeholders like `{player}` and `{world}`, plus static placeholders from `config.yml` like `{server-name}` and `{discord}`. Integrates with PlaceholderAPI when present.

## Plugins Using This Library

All Kukso plugins are adopting KuksoLib over time. Current state:

- **KuksoWorlds** — requires KuksoLib.
- **KuksoItems** — optional integration (soft dependency).
- **KuksoDialogs** — not integrated yet; planned.

---

## Installation

1. KuksoLib requires **ProtocolLib**; install it first.
2. Drop the KuksoLib jar into your `plugins` folder and restart the server.

Optional integrations are picked up automatically when present: PlaceholderAPI, KuksoItems, RealisticSeasons.

---

## Config.yml Example 🔧

```yaml
# Static variables. These cannot be changed during runtime. You must stop the server to make changes here
server-name: kukso.net
fallback-language: en # used when the player's client locale is not supported
logging-enabled: false # enable/disable logging into a separate file under /plugins/KuksoLib (universal)
debug-mode: false # enable/disable DEBUG mode

# Localization
supported_langs:
  - 'en'
  - 'tr'

# Variables (as placeholders) that are used in the localization strings
prefix: "#ffc13b[KuksoLib] &r"
discord: "https://discord.gg/Hqq3CdnenN" # Chat and announcements
telegram: "" # Announcements and replies
mobile-app: "" # In-app purchases and spending shop
```

The full default config also contains a `commands` section that maps aliases and permissions for each subcommand of `/kukso`, and a `gui` section reserved for the planned GUI module.

---

## Example API Usage 🧪

```java
KuksoAPI api = Bukkit.getServicesManager().load(KuksoAPI.class);
api.send(player, "kuksolib.welcome", Map.of("world", "Wilderness"));
```

Given this language entry:

```yaml
kuksolib:
  welcome: "{prefix}Welcome, {player}! You are in {world}."
```

`{prefix}` and `{player}` are filled by KuksoLib; `{world}` comes from the call.

---

## Planned Features

Not implemented yet — tracked in the repository backlog (`docs/sprints/backlog.md`):

- REST API (the `restful-enabled` config flag is reserved for it)
- GUI API (paged layouts, borders, close/back buttons)
- Auto-updater (today `/kukso ver` performs a manual version check)

---

## Terms and Conditions

This plugin is under the All Rights Reserved license (see the repository-root [LICENSE](../../LICENSE)). You are not permitted to:

- Copy, reproduce, or redistribute the Software or any portion of it.
- Modify, adapt, or create derivative works based on the Software.
- Use the Software or any part of it in commercial or non-commercial projects.
- Remove or alter any copyright, trademark, or other proprietary notices.
- Claim ownership of the Software or its components.

---

## Support & Contributions

Found an issue or want to contribute? Reach out via Spigot, Discord, or open a request on the GitHub repository.
