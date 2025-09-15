package io.github.devbd1.CubDialogs.commands.sub;

import io.github.devbd1.CubDialogs.commands.CmdConfig;
import io.github.devbd1.CubDialogs.commands.CmdInterface;
import org.bukkit.command.CommandSender;

import java.util.List;

public class VersionCmd implements CmdInterface {
    String CMD_NAME = "version";

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
        return "Shows plugin version, API version, and checks the latest tag on GitHub.";
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
        return false;
    }
}
