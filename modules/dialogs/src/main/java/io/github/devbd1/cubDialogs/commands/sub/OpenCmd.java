package io.github.devbd1.cubDialogs.commands.sub;

import io.github.devbd1.cubDialogs.commands.CmdConfig;
import io.github.devbd1.cubDialogs.commands.CmdInterface;
import io.github.devbd1.cubDialogs.program.DialogBuilder;
import io.github.devbd1.cubDialogs.program.DialogConfigManager;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OpenCmd implements CmdInterface {
    String CMD_NAME = "open";

    /**
     * Gets the primary name of this command.
     *
     * @return The command's name
     */
    @Override
    public String getName() {
        return CMD_NAME;
    }

    /**
     * Gets alternative names (aliases) for this command.
     * Note: This method is kept for backward compatibility, but aliases are now
     * primarily managed through config.yml via CommandConfig.getAliases().
     *
     * @return List of command aliases
     */
    @Override
    public List<String> getAliases() {
        return CmdConfig.getAliases(CMD_NAME);
    }

    /**
     * Gets the list of permissions required to use this command.
     * Note: This method is kept for backward compatibility, but permissions are now
     * primarily managed through config.yml via CommandConfig.getPermissions().
     *
     * @return List of permission strings
     */
    @Override
    public List<String> getPermissions() {
        return CmdConfig.getPermissions(CMD_NAME);
    }

    /**
     * Gets the command description.
     *
     * @return Command description
     */
    @Override
    public String getDescription() {
        return CmdInterface.super.getDescription();
    }

    /**
     * Gets the command usage syntax.
     *
     * @return Command usage string
     */
    @Override
    public String getUsage() {
        return CmdInterface.super.getUsage();
    }

    /**
     * Provides tab completion suggestions for this command.
     * Note: Permission checking is handled by CommandManager before this method is called.
     *
     * @param sender The command sender
     * @param args   Current command arguments
     * @return List of suggestions
     */
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

    /**
     * Executes the command logic.
     * Note: Permission checking is handled by CommandManager before this method is called.
     *
     * @param sender The command sender
     * @param args   The command arguments
     * @return true if the command was executed successfully
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        // Opens the dialog specified by args[0]; falls back to default if none provided.
        return new DialogBuilder().build(sender, args);
    }
}
