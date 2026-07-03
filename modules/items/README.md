# KuksoItems

✨ Custom item framework for Minecraft servers.  
🧱 Define items via YAML.  
🌐 Supports [KuksoLib](https://github.com/DevBD1/KuksoItems) for localized client-side lore.  
🔗 Works seamlessly with ProtocolLib.

---

## ✨ Features

- 📦 YAML-defined custom items
- 🏷️ Automatic NBT tagging for identification
- 🧙 Dynamic lore powered by [KuksoLib](https://github.com/DevBD1/KuksoItems)
- 🌍 Multilingual support with fallback
- 🚀 Fully client-side fake lore with ProtocolLib
- 💡 Easy item distribution with `/giveitem <key>`

---

## 📥 Installation

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

## 🧩 Requirements
- Spigot or Paper 1.21+
- ProtocolLib
- KuksoLib (optional but recommended)

---

## 🛠 Developers

Use the kuksoItems NBT key to identify and extend custom items across your plugin ecosystem.

---

## 📄 License
All Rights Reserved. See the repository-root [LICENSE](../../LICENSE) file.

---

## 🔧 Example `items.yml`

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
        - "&7Keskin bir kılıç."
        - "&c+10 Güç"
    nbt:
      customKey: strength_sword


