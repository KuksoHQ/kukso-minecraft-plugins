# KuksoDialogs

KuksoDialogs lets server owners build player-facing Minecraft dialog screens from YAML files. It loads dialog definitions from `plugins/KuksoDialogs/dialogs/`, validates them, opens them through commands, and exposes an action-listener API so addons can react to custom dialog submissions.

## Features And Utilities

- YAML-defined dialog screens loaded from the `dialogs/` folder.
- Built-in dialog types for notice, confirmation, and multi-action flows.
- Configurable bodies, inputs, buttons, exit buttons, permissions, and enabled/disabled states.
- Supported input styles include text, multiline text, number ranges, booleans, and single-option selections.
- Button actions for close, show dialog, open URL, player commands, console commands, suggested commands, and custom action keys.
- Bundled example dialogs and templates, including feedback, server rules, exp config, notice, confirmation, reward notice, and multi-action examples.
- `/kuksodialogs` command family for opening dialogs, forcing dialogs to open for another player, validating dialog configs, reloading dialog configs, checking version info, and listing loaded addons.
- Server Links support from `config.yml` for Minecraft's server links menu.
- Public `KuksoDialogsAPI` action registry for registering listeners by `DialogKey`.
- ServiceLoader-based addon loading from `plugins/KuksoDialogs/addons/`.

## Installation

1. Drop `KuksoDialogs-Paper-<version>.jar` into the server `plugins/` folder.
2. Start the server once so `plugins/KuksoDialogs/config.yml` and `plugins/KuksoDialogs/dialogs/` are generated.
3. Edit or add dialog YAML files under `plugins/KuksoDialogs/dialogs/`.
4. Run `/kuksodialogs validate` to check dialog configuration.
5. Use `/kuksodialogs reload` after dialog YAML changes. Server links and command aliases currently require a server restart.

PlaceholderAPI is optional.

## Usage Examples

```sh
/kuksodialogs open feedback_form
/kuksodialogs open server_rules Steve
/kuksodialogs forceopen exp_config Steve
/kuksodialogs validate
/kuksodialogs addons
```

```yaml
title: "<gold>Feedback Form</gold>"
type: "confirmation"
permission_to_open: "kuksodialogs.dialog.open.feedback"

inputs:
  - id: "feedback"
    type: "text"
    label: "Feedback"

exit_buttons:
  confirm:
    text: "Submit"
    action:
      type: "custom"
      key: "kukso:feedback/submit"
  cancel:
    text: "Cancel"
    action:
      type: "return"
```

## Example API Usage

```java
DialogActionRegistry registry = KuksoDialogsAPI.get();
Registration registration = registry.register(
    DialogKey.parse("kukso:feedback/submit"),
    context -> context.reply("Thanks for the feedback.")
);
```

Addons should implement `KuksoDialogsAddon`, register listeners in `onEnable(AddonContext context)`, and provide a `META-INF/services/com.kukso.minecraft.dialogs.API.addon.KuksoDialogsAddon` entry.

## Terms And Conditions

This plugin is proprietary and All Rights Reserved. See the repository-root [LICENSE](../../LICENSE). You may not copy, redistribute, modify, adapt, remove notices from, or claim ownership of the software unless Kukso grants written permission.

## Support & Contributions

Found an issue or want to request a change? Reach out through the Kukso support channels or open a request on the GitHub repository.
