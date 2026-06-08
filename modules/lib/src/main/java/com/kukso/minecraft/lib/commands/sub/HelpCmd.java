package com.kukso.minecraft.lib.commands.sub;

import com.kukso.minecraft.lib.commands.CmdInterface;
import com.kukso.minecraft.lib.commands.CmdConfig;
import com.kukso.minecraft.lib.commands.CmdManager;

import com.kukso.minecraft.lib.modules.logger.LoggingManager;
import org.bukkit.command.CommandSender;
import java.util.List;
import java.util.Collection;

public class HelpCmd implements CmdInterface {
    String CMD_NAME = "help";
    private final LoggingManager logger;
    private final CmdManager manager;

    public HelpCmd(CmdManager manager, LoggingManager logger) {
        this.manager = manager;
        this.logger = logger;
    }

    @Override
    public String getName() {
        return CMD_NAME;
    }

    @Override
    public List<String> getPermissions()
    {
        return CmdConfig.getPermissions(CMD_NAME);
    }

    @Override
    public List<String> getAliases() {
        return CmdConfig.getAliases(CMD_NAME);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        logger.info("Â§6[KuksoLib] Â§f'/kukso help' command used by Â§e" + sender.getName());

        sender.sendMessage("Â§8Â§m------Â§r Â§aKuksoLib Help Â§8Â§m------");
        if (manager == null) return true;
    
    Collection<CmdInterface> commands = manager.getCommands();
    for (CmdInterface cmd : commands) {
        List<String> permissions = cmd.getPermissions();
        boolean hasPermission = permissions.isEmpty() || 
        permissions.stream().anyMatch(sender::hasPermission);
        
        if (!hasPermission) continue;
        
        sender.sendMessage("Â§e" + cmd.getUsage() + " Â§7- " + cmd.getDescription());
    }
    return true;
}

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return List.of();
    }

    @Override
    public String getDescription() {
        return "Shows this help menu.";
    }
}