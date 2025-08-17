package io.github.devbd1.cubDialogs.commands.sub;

import io.github.devbd1.cubDialogs.commands.CmdInterface;
import io.github.devbd1.cubDialogs.utilities.DialogConfigValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ValidateCmd implements CmdInterface {
    
    @Override
    public String getName() {
        return "validate";
    }

    @Override
    public List<String> getAliases() {
        return List.of("check", "verify");
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
        return List.of();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("§6Validating dialog configurations...");
        
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(ValidateCmd.class);
        List<DialogConfigValidator.ValidationIssue> issues = DialogConfigValidator.validateAllDialogs(plugin);
        
        DialogConfigValidator.sendValidationResults(sender, issues);
        
        return true;
    }

    @Override
    public String getDescription() {
        return "Validates all dialog configurations for common issues";
    }

    @Override
    public String getUsage() {
        return "/cubDialogs validate";
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
        return List.of();
    }
}
