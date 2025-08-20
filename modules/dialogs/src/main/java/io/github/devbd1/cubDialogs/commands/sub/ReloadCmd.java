package io.github.devbd1.cubDialogs.commands.sub;

import io.github.devbd1.cubDialogs.commands.CmdConfig;
import io.github.devbd1.cubDialogs.commands.CmdInterface;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCmd implements CmdInterface {
    String CMD_NAME = "reload";
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
        return List.of();
    }


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        try {
            // Reload the main plugin configuration
            if (io.github.devbd1.cubDialogs.Main.getInstance() != null) {
                io.github.devbd1.cubDialogs.Main.getInstance().reloadConfig();
            }

            // Reload all dialog configurations
            io.github.devbd1.cubDialogs.dialog.DialogConfigManager.reloadDialogConfigs();

            // Send a success message to the command sender
            sender.sendMessage("§aConfiguration, and dialogs reloaded successfully! You need to restart the server to reload server links and command aliases.");

            return true;
        } catch (Exception e) {
            sender.sendMessage("§cFailed to reload configuration: " + e.getMessage());
            return false;
        }
    }
}
