# KuksoDialogsExpConfigAddon

A service-loaded example addon for KuksoDialogs that exposes experience/config values to dialogs.

## 🧩 Requirements

- Paper 1.21+
- KuksoDialogs

## 🛠 Build

```
./gradlew :dialogs-exp-config-addon:build
```

## 📥 Install

KuksoDialogs discovers addons via `ServiceLoader`, not the server's regular `/plugins` folder. Stop the server, drop the built jar into KuksoDialogs' own `addons` folder (`plugins/KuksoDialogs/addons/`), then start the server — KuksoDialogs loads any jar there that provides a `KuksoDialogsAddon` service implementation.

See the addons system notes in [../dialogs/README.md](../dialogs/README.md) for more on how KuksoDialogs addons work.
