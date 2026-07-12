# KuksoVotes

KuksoVotes is a voting-rewards plugin for Minecraft servers. It aims to be a
vote-site-agnostic, Votifier-compatible replacement for the traditional
Votifier + reward-plugin stack, delivered as a single drop-in jar.

> **Status:** early alpha (`0.1.0-alpha.1`). Only the command scaffold described
> below is implemented today. Vote-protocol handling and reward distribution are
> tracked in the repository backlog and are not yet available.

## Features And Utilities

- `/kuksovotes` (alias `/kv`) command with `version`, `reload`, and help output.
- Self-contained localization with bundled English and Turkish language files
  under `lang/`, selectable via `language` in `config.yml`.
- Runtime reload of configuration and language files via `/kuksovotes reload`.
- Optional KuksoLib soft dependency: KuksoVotes runs fully standalone and uses
  KuksoLib only for ecosystem integration when it is present.

## Installation

1. Drop `KuksoVotes-Paper-<version>.jar` into the server `plugins/` folder.
2. Start the server once so `plugins/KuksoVotes/config.yml` and the `lang/`
   files are generated.
3. (Optional) Set `language` in `config.yml`, then run `/kuksovotes reload`.

KuksoLib is optional and is not required for any current KuksoVotes command.

## Usage Examples

```sh
/kuksovotes version
/kv reload
```

Permissions:

```text
kuksovotes.command.root   # access to /kuksovotes (default: everyone)
kuksovotes.admin          # administrative subcommands such as reload (default: op)
```

## Terms And Conditions

This plugin is proprietary and All Rights Reserved. See the repository-root
[LICENSE](../../LICENSE). You may not copy, redistribute, modify, adapt, remove
notices from, or claim ownership of the software unless Kukso grants written
permission.

## Support & Contributions

Found an issue or want to request a change? Reach out through the Kukso support
channels or open a request on the GitHub repository.
