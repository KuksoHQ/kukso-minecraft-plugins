package com.kukso.minecraft.worlds;

import com.kukso.minecraft.worlds.modules.GriefPrevention;
import com.kukso.minecraft.worlds.modules.WorldAccess;
import org.bukkit.Server;

public class EventRegistrar {
    public static void register(Server server, Main plugin) {
        server.getPluginManager().registerEvents(new GriefPrevention(plugin), plugin);
        server.getPluginManager().registerEvents(new WorldAccess(plugin), plugin);
    }
}
