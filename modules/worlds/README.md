# KuksoWorlds

KuksoWorlds is a lightweight world-management plugin for Paper/Spigot servers. It loads configured worlds, applies world settings, provides admin commands for world lifecycle operations, and uses KuksoLib for localized command output.

## Features And Utilities

- Config-driven world definitions with world type, spawn location, grief-prevention flag, and world-border settings.
- Startup loading for worlds listed in `config.yml`.
- Commands for creating, loading, unloading, deleting, listing, teleporting to, and setting spawn points for worlds.
- Safe fallback-world handling when players need to be moved before unload or delete operations.
- Recycle bin for deleted world folders, with list, restore, and empty actions.
- Void-world generator support.
- Optional KuksoLib-backed custom logging toggle.
- World-access checks based on configured permissions.
- PlaceholderAPI expansion when PlaceholderAPI is installed.
- English and Turkish language files loaded through KuksoLib.

## Installation

1. Install KuksoLib first.
2. Drop `KuksoWorlds-Paper-<version>.jar` into the server `plugins/` folder.
3. Start the server once so `plugins/KuksoWorlds/config.yml` and language files are generated.
4. Configure `fallback-world` and the `worlds:` list.
5. Restart the server after config changes.

PlaceholderAPI is optional.

## Usage Examples

```sh
/kuksoworlds list
/kuksoworlds create arena VOID prevent-grief:true world-border:500
/kuksoworlds load arena
/kuksoworlds teleport arena
/kuksoworlds setspawn
/kuksoworlds setspawn confirm
/kuksoworlds unload arena
/kuksoworlds delete arena
/kuksoworlds recyclebin list
/kuksoworlds recyclebin restore arena
```

Example `config.yml`:

```yaml
fallback-world: world_spawn

modules:
  custom-logger: true

worlds:
  - name: world_spawn
    type: VOID
    prevent-grief: true
    world-border:
      size: 1010
      center:
        x: 0
        z: 0
    spawn:
      x: 0.5
      y: 0
      z: -62.5
      yaw: 0
      pitch: -5.0
```

## Example API Usage

KuksoWorlds does not currently expose a public module API. Other plugins should interact through Bukkit world APIs and KuksoWorlds commands.

## Terms And Conditions

This plugin is proprietary and All Rights Reserved. See the repository-root [LICENSE](../../LICENSE). You may not copy, redistribute, modify, adapt, remove notices from, or claim ownership of the software unless Kukso grants written permission.

## Support & Contributions

Found an issue or want to request a change? Reach out through the Kukso support channels or open a request on the GitHub repository.
