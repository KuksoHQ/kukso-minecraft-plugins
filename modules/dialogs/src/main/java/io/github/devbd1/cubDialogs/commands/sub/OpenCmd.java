package io.github.devbd1.cubDialogs.commands.sub;

import io.github.devbd1.cubDialogs.commands.CmdConfig;
import io.github.devbd1.cubDialogs.commands.CmdInterface;
import io.github.devbd1.cubDialogs.features.dialog.DialogBuilder;
import io.github.devbd1.cubDialogs.features.dialog.DialogConfigManager;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OpenCmd implements CmdInterface {
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
        // Suggest dialog IDs for the first argument
        if (args.length <= 1) {
            String prefix = args.length == 0 ? "" : args[0].toLowerCase(Locale.ROOT);

            List<String> ids = new ArrayList<>(DialogConfigManager.getDialogIds()); // assumes a method returning dialog IDs
            ids.sort(String::compareToIgnoreCase);

            if (prefix.isEmpty()) return ids;
            return ids.stream()
                    .filter(id -> id.toLowerCase(Locale.ROOT).startsWith(prefix))
                    .toList();
        }
        return List.of();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        // Opens the dialog specified by args[0]; falls back to default if none provided.
        return new DialogBuilder().build(sender, args);
    }
}
