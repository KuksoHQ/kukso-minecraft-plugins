package io.github.devbd1.CubDialogs.commands.sub;

import io.github.devbd1.CubDialogs.commands.CmdInterface;
import org.bukkit.command.CommandSender;

import java.util.List;

public class VersionCmd implements CmdInterface {
    @Override
    public String getName() {
        return "";
    }

    @Override
    public List<String> getAliases() {
        return List.of();
    }

    @Override
    public List<String> getPermissions() {
        return List.of();
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
        return false;
    }
}
