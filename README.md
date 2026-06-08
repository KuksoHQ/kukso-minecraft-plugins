# Kukso Minecraft

Gradle monorepo for Kukso Minecraft plugins.

## Modules

- `:lib` - `KuksoLib`, the shared Minecraft plugin library.
- `:dialogs` - `KuksoDialogs`.
- `:worlds` - `KuksoWorlds`.
- `:items` - `KuksoItems`.

## Build

```powershell
.\gradlew.bat projects
.\gradlew.bat build
```

Release tags use `repo-module-vX.Y.Z`, for example `kukso-minecraft-worlds-v1.2.0`.
Release jars use `KuksoModule-Platform-X.Y.Z.jar`, for example `KuksoWorlds-Paper-1.2.0.jar`.
