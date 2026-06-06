package io.github.devbd1.CubDialogs.commands.sub;

import io.github.devbd1.CubDialogs.Main;
import io.github.devbd1.CubDialogs.API.addon.AddonManager;
import io.github.devbd1.CubDialogs.commands.CmdConfig;
import io.github.devbd1.CubDialogs.commands.CmdInterface;
import org.bukkit.command.CommandSender;

import java.util.List;

public class AddonsCmd implements CmdInterface {
    String CMD_NAME = "addons";

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
        return "List loaded addons";
    }

    @Override
    public String getUsage() {
        return "/" + getName();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return List.of();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        AddonManager addonManager = Main.getInstance().getAddonManager();
        List<AddonManager.AddonInfo> addons = addonManager.getLoadedAddonInfo();
        
        if (addons.isEmpty()) {
            sender.sendMessage("No addons are currently loaded.");
            sender.sendMessage("Place addon JARs in plugins/CubDialogs/addons");
            return true;
        }
        
        sender.sendMessage("§6=== Loaded Addons ===");
        for (AddonManager.AddonInfo addon : addons) {
            sender.sendMessage("§e" + addon.getId() + " §7v" + addon.getVersion());
        }
        sender.sendMessage("§6" + addons.size() + " addon(s) loaded");
        
        return true;
    }
}
