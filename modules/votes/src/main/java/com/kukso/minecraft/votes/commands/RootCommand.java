package com.kukso.minecraft.votes.commands;

import com.kukso.minecraft.votes.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Root {@code /kuksovotes} command. Currently a scaffold exposing version,
 * reload, and help; reward and vote-site subcommands are added in later phases.
 */
public class RootCommand implements CommandExecutor {

    private final Main plugin;

    public RootCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "version" -> {
                sender.sendMessage(plugin.lang().prefixed("commands.version.info",
                        "version", plugin.getPluginMeta().getVersion(),
                        "authors", String.join(", ", plugin.getPluginMeta().getAuthors())));
                sender.sendMessage(plugin.lang().prefixed(plugin.isKuksoLibPresent()
                        ? "commands.version.kuksolib-present"
                        : "commands.version.kuksolib-absent"));
            }
            case "reload" -> {
                if (!sender.hasPermission("kuksovotes.admin")) {
                    sender.sendMessage(plugin.lang().prefixed("general.no-permission"));
                    return true;
                }
                plugin.reloadConfig();
                plugin.lang().reload();
                sender.sendMessage(plugin.lang().prefixed("commands.reload.success"));
            }
            default -> sender.sendMessage(plugin.lang().prefixed("general.unknown-subcommand",
                    "command", args[0]));
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(plugin.lang().message("commands.help.header"));
        sender.sendMessage(plugin.lang().message("commands.help.version"));
        sender.sendMessage(plugin.lang().message("commands.help.reload"));
    }
}
