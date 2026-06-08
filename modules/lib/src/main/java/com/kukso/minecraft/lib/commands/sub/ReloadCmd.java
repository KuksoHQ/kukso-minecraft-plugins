package com.kukso.minecraft.lib.commands.sub;

import com.kukso.minecraft.lib.commands.CmdInterface;
import com.kukso.minecraft.lib.commands.CmdConfig;
import com.kukso.minecraft.lib.modules.logger.LoggingManager;
import com.kukso.minecraft.lib.modules.text.Lang;
import com.kukso.minecraft.lib.utilities.ConfigManager;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class ReloadCmd implements CmdInterface {
    String CMD_NAME = "reload";
    private final JavaPlugin plugin;
    private final LoggingManager logger;

    public ReloadCmd(JavaPlugin plugin, LoggingManager logger) {
        this.plugin = plugin;
        this.logger = logger;
    }

    @Override
    public String getName() {
        return CMD_NAME;
    }

    @Override
    public List<String> getPermissions() {
        return CmdConfig.getPermissions(CMD_NAME);
    }

    @Override
    public List<String> getAliases() {
        return CmdConfig.getAliases(CMD_NAME);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return List.of();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        logger.info("Â§6[KuksoLib] Â§f'/kukso reload' command used by Â§e" + sender.getName());

        plugin.reloadConfig();
        ConfigManager.getInstance().init(plugin);
        Lang.load(plugin);

        ConfigManager.getInstance().printStatus(plugin);

        sender.sendMessage("Â§6KuksoLib Â§freloaded.");
        // Lang.send(sender, "command.reload.success");
        // Bukkit.getLogger().info("Â§a[KuksoLib] Reloaded by " + sender.getName());
        logger.info("Â§6[KuksoLib] Â§fReloaded by Â§e" + sender.getName());
        return true;
    }

}
