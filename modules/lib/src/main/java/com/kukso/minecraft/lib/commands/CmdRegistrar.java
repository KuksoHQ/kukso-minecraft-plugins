package com.kukso.minecraft.lib.commands;

import com.kukso.minecraft.lib.Main;
import com.kukso.minecraft.lib.commands.sub.*;
import com.kukso.minecraft.lib.commands.test.*;
import com.kukso.minecraft.lib.modules.logger.LoggingManager;
import org.bukkit.command.PluginCommand;

public final class CmdRegistrar {
    private CmdRegistrar() {}

    public static void register(Main plugin, LoggingManager logger) {
        PluginCommand cmd = plugin.getCommand("kukso");
        if (cmd == null) {
            logger.severe("COMMAND '/kukso' NOT FOUND! Aborting command registration and disabling the plugin.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        CmdManager mgr = new CmdManager();
        mgr.register(new GetKeyValueCmd());
        mgr.register(new HelpCmd(mgr, logger));
        mgr.register(new ReloadCmd(plugin, logger));
        mgr.register(new VersionCmd(plugin, logger));
        mgr.register(new TestLoggerCmd(plugin, logger));

        cmd.setExecutor(mgr);
        cmd.setTabCompleter(mgr);
        logger.info("Commands registered.");
    }
}