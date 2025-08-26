package io.github.devbd1.cubDialogs.commands.sub;

import io.github.devbd1.cubDialogs.commands.CmdConfig;
import io.github.devbd1.cubDialogs.commands.CmdInterface;
import io.github.devbd1.cubDialogs.dialog.DialogBuilder;
import io.github.devbd1.cubDialogs.dialog.DialogConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OpenCmd implements CmdInterface {
    private final JavaPlugin plugin;

    public OpenCmd(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    String CMD_NAME = "open";

    @Override
    public String getName() {
        return CMD_NAME;
    }

    @Override
    public List<String> getAliases() {
        return CmdConfig.getAliases(CMD_NAME);
    }

    @Override
    public List<String> getPermissions() {
        return CmdConfig.getPermissions(CMD_NAME);
    }

    @Override
    public String getDescription() {
        return CmdInterface.super.getDescription();
    }

    @Override
    public String getUsage() {
        return CmdInterface.super.getUsage();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        // args[0] -> dialog id
        // args[1] -> player name
        if (args.length == 0 || args.length == 1) {
            String prefix = args.length == 0 ? "" : args[0].toLowerCase(Locale.ROOT);
            List<String> ids = new ArrayList<>(DialogConfigManager.getDialogIds());
            ids.sort(String::compareToIgnoreCase);
            if (prefix.isEmpty()) return ids;
            return ids.stream()
                    .filter(id -> id.toLowerCase(Locale.ROOT).startsWith(prefix))
                    .toList();
        } else if (args.length == 2) {
            String prefix = args[1].toLowerCase(Locale.ROOT);
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(prefix))
                    .sorted(String::compareToIgnoreCase)
                    .toList();
        }
        return List.of();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        // 1) Resolve dialog ID
        final String id = args.length > 0 ? args[0] : "feedback_form";
        if (!DialogConfigManager.hasDialog(id)) {
            sender.sendMessage("§cCould not build dialog: " + id + " (check dialogs folder)");
            plugin.getLogger().warning("Dialog configuration not found for ID: " + id);
            return false;
        }

        // 2) Check enabled flag and give feedback
        if (!DialogConfigManager.isEnabled(id)) {
            sender.sendMessage("§eThis dialog is currently disabled: §6" + id);
            return false;
        }

        // 3) Resolve permissions from config
        final String permOpen = DialogConfigManager.getPermissionToOpen(id);
        final String permRemote = DialogConfigManager.getPermissionToOpenRemote(id);

        // 4) Resolve target viewer (self vs remote)
        final boolean hasTarget = args.length > 1;
        Player viewer;

        if (hasTarget) {
            String targetName = args[1];
            viewer = Bukkit.getPlayerExact(targetName);
            if (viewer == null) {
                sender.sendMessage("§cPlayer '" + targetName + "' is not online or does not exist.");
                return false;
            }

            // Sender must be allowed to open remotely
            if (!sender.hasPermission(permRemote)) {
                sender.sendMessage("§cYou do not have permission to open this dialog remotely.");
                return true;
            }

            // Also ensure the target viewer is permitted to view this dialog
            if (!viewer.hasPermission(permOpen)) {
                sender.sendMessage("§cThat player does not have permission to view this dialog.");
                return false;
            }
        } else {
            if (!(sender instanceof Player p)) {
                sender.sendMessage("§eThis command can only be used by players (or specify an online player).");
                return true;
            }
            viewer = p;

            // Ensure the viewer (self) is allowed to open
            if (!viewer.hasPermission(permOpen)) {
                viewer.sendMessage("§cYou do not have permission to open this dialog.");
                return false;
            }
        }

        // 5) Build and show dialog to the resolved viewer
        return new DialogBuilder().build(viewer, id);
    }
}