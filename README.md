# Kukso Minecraft

Gradle Kotlin DSL monorepo for Kukso Minecraft Paperspigot plugins.

## Modules

- `:lib` - `KuksoLib`, the shared Minecraft plugin library.
- `:dialogs` - `KuksoDialogs`.
- `:dialogs-exp-config-addon` - ExpConfig addon for KuksoDialogs.
- `:worlds` - `KuksoWorlds`.
- `:items` - `KuksoItems`.

## Build

```powershell
.\gradlew.bat projects
.\gradlew.bat build
```

Release tags use `repo-module-vX.Y.Z`, for example `kukso-minecraft-worlds-v1.2.0`.
Release jars use `KuksoModule-Platform-X.Y.Z.jar`, for example `KuksoWorlds-Paper-1.2.0.jar`.

## License

Licensed under the Apache License 2.0. See [LICENSE](LICENSE). This license applies to all modules in this repository.
