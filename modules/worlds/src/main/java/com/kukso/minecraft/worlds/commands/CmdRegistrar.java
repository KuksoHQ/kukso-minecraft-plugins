package com.kukso.minecraft.worlds.commands;

import com.kukso.minecraft.worlds.Main;
import com.kukso.minecraft.worlds.commands.sub.*;
import org.bukkit.command.PluginCommand;

import java.util.List;

public class CmdRegistrar {

    private CmdRegistrar() {}

    public static void register(Main plugin) {
        PluginCommand cmd = plugin.getCommand("kuksoworlds");
        if (cmd == null) {
            plugin.getLogger().severe("COMMAND 'kuksoworlds' NOT FOUND! Make sure it's properly defined in plugin.yml.");
            plugin.getDescription().getCommands().keySet().forEach(command ->
                plugin.getLogger().info("- " + command)
            );
            return;
        }

        CmdManager mgr = new CmdManager();
        for (CmdInterface command : List.of(
                new VersionCmd(plugin),
                new RecycleCmd(plugin),
                new CreateCmd(plugin),
                new DeleteCmd(plugin),
                new ListCmd(plugin),
                new LoadCmd(plugin),
                new TeleportCmd(plugin),
                new UnloadCmd(plugin),
                new SetSpawnCmd(plugin)
        )) {
            mgr.register(command);
        }

        cmd.setExecutor(mgr);
        cmd.setTabCompleter(mgr);
    }
}
