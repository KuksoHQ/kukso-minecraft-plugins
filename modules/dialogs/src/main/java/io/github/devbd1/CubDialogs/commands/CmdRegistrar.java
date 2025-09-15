package io.github.devbd1.CubDialogs.commands;

import io.github.devbd1.CubDialogs.Main;
import io.github.devbd1.CubDialogs.commands.sub.*;
import org.bukkit.command.PluginCommand;

public class CmdRegistrar {
    private CmdRegistrar() {}

    public static void register(Main plugin) {
        PluginCommand cmd = plugin.getCommand("cubDialogs");
        if (cmd == null) {
            //logger.severe("COMMAND '/cubDialogs' NOT FOUND! Aborting command registration and disabling the plugin.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        CmdManager mgr = new CmdManager();
        mgr.register(new OpenCmd(plugin));
        mgr.register(new ForceOpenCmd(plugin));
        mgr.register(new ValidateCmd());
        mgr.register(new ReloadCmd());
        mgr.register(new VersionCmd());

        cmd.setExecutor(mgr);
        cmd.setTabCompleter(mgr);
    }
}
