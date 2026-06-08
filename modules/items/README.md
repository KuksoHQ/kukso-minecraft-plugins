# KuksoItems

âœ¨ Custom item framework for Minecraft servers.  
ðŸ§± Define items via YAML.  
ðŸŒ Supports [KuksoLib](https://github.com/DevBD1/KuksoItems) for localized client-side lore.  
ðŸ”— Works seamlessly with ProtocolLib.

---

## âœ¨ Features

- ðŸ“¦ YAML-defined custom items
- ðŸ·ï¸ Automatic NBT tagging for identification
- ðŸ§™ Dynamic lore powered by [KuksoLib](https://github.com/DevBD1/KuksoItems)
- ðŸŒ Multilingual support with fallback
- ðŸš€ Fully client-side fake lore with ProtocolLib
- ðŸ’¡ Easy item distribution with `/giveitem <key>`

---

## ðŸ“¥ Installation

- Stop the server
- Place ```KuksoItems.jar``` in ```/plugins```
- Install [ProtocolLib](https://ci.dmulloy2.net/job/ProtocolLib/)
- Install [KuksoLib](https://github.com/DevBD1/KuksoItems) if you want client-side lore implementation
- Start the server

---

Commands | Description | Permission | Group
--- | --- | --- | --- 
/giveitem <key> | Gives a registered item | cubitems.giveitem | cubitems.admin

---

## ðŸ§© Requirements
- Spigot or Paper 1.21+
- ProtocolLib
- KuksoLib (optional but recommended)

---

## ðŸ›  Developers

Use the kuksoItems NBT key to identify and extend custom items across your plugin ecosystem.

---

## ðŸ“„ License
MIT â€” do what you want, give credit.

---

## ðŸ”§ Example `items.yml`

```yaml
items:
  strength_sword:
    material: DIAMOND_SWORD
    display_name: "&cSword of Strength"
    lore:
      en:
        - "&7A sharp blade."
        - "&c+10 Strength"
      tr:
        - "&7Keskin bir kÄ±lÄ±Ã§."
        - "&c+10 GÃ¼Ã§"
    nbt:
      customKey: strength_sword


