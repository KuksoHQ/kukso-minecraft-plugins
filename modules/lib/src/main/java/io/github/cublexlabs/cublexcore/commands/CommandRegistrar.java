package io.github.cublexlabs.cublexcore.commands;

import io.github.cublexlabs.cublexcore.Main;
import io.github.cublexlabs.cublexcore.commands.sub.*;
import io.github.cublexlabs.cublexcore.commands.test.*;
import io.github.cublexlabs.cublexcore.modules.logger.LoggingManager;
import org.bukkit.command.PluginCommand;

public final class CommandRegistrar {
    private CommandRegistrar() {}

    public static void register(Main plugin, LoggingManager logger) {
        PluginCommand cmd = plugin.getCommand("cublex");
        if (cmd == null) {
            logger.severe("COMMAND '/cublex' NOT FOUND! Aborting command registration and disabling the plugin.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        CommandManager mgr = new CommandManager();
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