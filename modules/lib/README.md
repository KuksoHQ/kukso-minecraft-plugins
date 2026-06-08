[![Wiki: 1.0](https://img.shields.io/badge/Wiki-Kukso_Labs-blue.svg)]([https://creativecommons.org/licenses/by/4.0/](https://labs.kukso.net/docs/category/common-in-all-plugins))

# KuksoLib
Modular API foundation for Kukso Labs plugins, centralizing core functions to simplify maintenance and reduce overall plugin size.

---
### Plugins using this library:
- KuksoDialogs as of 2.x version
- KuksoWorlds as of 2.x version

---
### Installation:
- Download, drop it in the plugins folder, and you are done.
- Additionally, you can change the auto-updater feature in the config file if you don't want it to be automatically updated.

---
### Core Features:
- ðŸŒ **Native Localization:** Supports YAML-based i18n with `{placeholders}`, automatic fallback to the `{default-language}`
- ðŸ§¾ **Centralized Log and Debug System:** Missing keys, fallback events, and in-game log viewing
- ðŸ“Œ **Placeholders:** Dynamic placeholders like {player}, {world}, {coins}, etc. and static placeholders from config.yml like {server-name}, {discord}, etc.

---
### Terms and conditions
Plugin is under the All rights reserved license. You are not permitted to:
- Copy, reproduce, or redistribute the Software or any portion of it.
- Modify, adapt, or create derivative works based on the Software.
- Use the Software or any part of it in commercial or non-commercial projects.
- Remove or alter any copyright, trademark, or other proprietary notices.
- Claim ownership of the Software or its components.

---
### Config.yml Example ðŸ”§
```yaml
# Static variables. These cannot be changed during runtime. You must stop the server to make changes here
server-name: kukso.net
fallback-language: en # it is used when the player's client locale is not supported
logging-enabled: false # enable/disable logging into a separate file under /plugins/KuksoLib folder (universal)
restful-enabled: false # enable/disable REST API on your server. Check https://dev.kukso.net/wiki/kuksolib/rest
debug-mode: false # enable/disable DEBUG mode

# Variables (as placeholders) that are used in the localization strings
prefix: "#ffc13b[KuksoLib] &r"
test-prefix: "&c[KuksoLib] &r"
discord: "https://discord.gg/Hqq3CdnenN" # Chat and announcements
telegram: "" # Announcements and replies
mobile-app: "" # In-app purchases and spending shop
```

---
### ðŸ§ª Example API Usage

```java
KuksoAPI api = Bukkit.getServicesManager().load(KuksoAPI.class);
api.send(player, "kuksolib.welcome", Map.of("world", "Wilderness"));
```
Because kuksolib.welcome has {prefix}, {player} and {world} placeholders. First 2 is filled by the program, and the last one given by the call.
```
kuksolib:
  welcome: "{prefix}Welcome, {player}! You are in {world}."
```

---
### Future Plans
- /kukso log recent command
- Client-side item-lore support for extreme localization
- GUI API (paged layouts, close/back buttons, border styles)
- Auto-merging of locale files on update
- Plugin usage metrics
- Announcement API (Game, Discord)
- AI module
- Use arguments in the permission-error message. Interaction or command error
- Format commands help into clickable TextComponents
- Paginate the output of help command for more commands
- Auto-suggest when the user types an invalid command
- Add force-default-language boolean

---
### Support & Contributions
Found an issue or want to contribute?
Reach out via Spigot, Discord or open a request on the GitHub repository.

---




NOTES ON GUI MODULE:
1- if 6 lines, have a border in slots [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 50, 51, 52, 53]
2- if 6 lines and multi page, have a border, and slots 50, 49 and 48 are for Previous page, Close this page, Next page buttons.
3- if 6 lines and single page, slot 49 is Close this page button
4- if 5, 4 or 2 lines ERROR
5- if 3 lines, have option to have border or not. single page or multi page. slots 23, 22, 21 are buttons.
6- if single line and single page, no border, all 9 slots are custom



