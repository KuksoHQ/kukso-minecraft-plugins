# KuksoItems

KuksoItems is a lightweight custom item plugin for Minecraft servers. It loads item definitions from `items.yml`, builds Bukkit `ItemStack` instances with display names, lore, and NBT tags, then lets players receive configured items with a simple command.

## Features And Utilities

- YAML-defined custom items under the top-level `items:` section.
- Material, display name, lore, and custom NBT fields per item.
- Internal `kuksoItems` NBT tag generated from `nbt.customKey`.
- Legacy `&` color-code formatting for display names and lore.
- `/giveitem <item-key>` command for giving configured items to players.
- `ItemManager` lookup utility for registered items.
- `ItemUtils` helpers for checking whether an `ItemStack` is a KuksoItems item and reading its custom item key.
- Optional KuksoLib soft dependency for ecosystem integration.

## Installation

1. Drop `KuksoItems-Paper-<version>.jar` into the server `plugins/` folder.
2. Start the server once so `plugins/KuksoItems/config.yml` and `plugins/KuksoItems/items.yml` are generated.
3. Edit `items.yml`.
4. Restart the server to reload item definitions.

KuksoLib is optional. It is not required for the `/giveitem` command or basic YAML item loading.

## Usage Examples

```sh
/giveitem strength_sword
```

Permission:

```text
kuksoitems.giveitem
```

Example `items.yml`:

```yaml
items:
  strength_sword:
    material: DIAMOND_SWORD
    display_name: "&cSword of Strength"
    lore:
      - "&7A sharp blade."
      - "&c+10 Strength"
    nbt:
      customKey: strength_sword
```

## Example API Usage

```java
ItemStack item = ItemManager.getItem("strength_sword");

if (ItemUtils.isCustomItem(item, "strength_sword")) {
    String key = ItemUtils.getCustomItemKey(item);
}
```

## Terms And Conditions

This plugin is proprietary and All Rights Reserved. See the repository-root [LICENSE](../../LICENSE). You may not copy, redistribute, modify, adapt, remove notices from, or claim ownership of the software unless Kukso grants written permission.

## Support & Contributions

Found an issue or want to request a change? Reach out through the Kukso support channels or open a request on the GitHub repository.
