# KuksoDialogsExpConfigAddon

KuksoDialogsExpConfigAddon is a service-loaded KuksoDialogs addon that handles the `kukso:exp_config/confirm` dialog action. It reads level, experience percentage, and optional target-player values from a dialog response, then applies the requested level and experience to the sender or target player.

## Features And Utilities

- Implements the KuksoDialogs `KuksoDialogsAddon` interface.
- Registers a listener for `kukso:exp_config/confirm`.
- Reads `level`, `experience`, and `player_name` fields from the submitted dialog payload.
- Applies player level and experience percentage through the Bukkit player API.
- Supports targeting another online player when `player_name` is provided.
- Cleans up registered listeners when the addon unloads.

## Installation

1. Build the addon with `./gradlew :dialogs-exp-config-addon:build`.
2. Stop the server.
3. Drop `KuksoDialogsExpConfigAddon-Paper-<version>.jar` into `plugins/KuksoDialogs/addons/`.
4. Start the server. KuksoDialogs discovers addons through Java `ServiceLoader`.

Do not place this addon in the server's regular `plugins/` folder; it is loaded by KuksoDialogs from its addon directory.

## Usage Examples

Use a KuksoDialogs YAML action with the matching key:

```yaml
action:
  type: "custom"
  key: "kukso:exp_config/confirm"
```

The dialog payload should include numeric `level` and `experience` values. If `player_name` is blank, the addon updates the submitting player.

## Example API Usage

This module is itself an example of the KuksoDialogs addon API:

```java
public final class ExpConfigAddon implements KuksoDialogsAddon {
    public void onEnable(AddonContext context) {
        Registration registration = KuksoDialogsAPI.get().register(
            DialogKey.parse("kukso:exp_config/confirm"),
            actionContext -> actionContext.reply("Handled exp config submit.")
        );
    }
}
```

## Terms And Conditions

This addon is proprietary and All Rights Reserved. See the repository-root [LICENSE](../../LICENSE). You may not copy, redistribute, modify, adapt, remove notices from, or claim ownership of the software unless Kukso grants written permission.

## Support & Contributions

Found an issue or want to request a change? Reach out through the Kukso support channels or open a request on the GitHub repository.
