package com.kukso.minecraft.dialogs.commands.sub;

import com.kukso.minecraft.dialogs.Main;
import com.kukso.minecraft.dialogs.API.addon.AddonManager;
import com.kukso.minecraft.dialogs.commands.CmdConfig;
import com.kukso.minecraft.dialogs.commands.CmdInterface;
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
        return "/kuksodialogs " + getName();
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
            sender.sendMessage("Place addon JARs in plugins/KuksoDialogs/addons");
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
