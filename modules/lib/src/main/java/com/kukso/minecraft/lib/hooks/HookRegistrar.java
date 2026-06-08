package com.kukso.minecraft.lib.hooks;

import com.kukso.minecraft.lib.Main;
import com.kukso.minecraft.lib.modules.logger.LoggingManager;
import com.kukso.minecraft.lib.hooks.PlaceholderAPI.PlaceholderApplier;
//import io.github.devbd1.kuksolib.hooks.KuksoItems.ClientSideTextAdapter;
//import io.github.devbd1.kuksolib.hooks.RealisticSeasons.Listener;
import org.bukkit.Bukkit;

public final class HookRegistrar {
    private HookRegistrar() {}

    public static void register(Main plugin, LoggingManager logger) {
        // PlaceholderAPI
        PlaceholderApplier.init(logger);

        // KuksoItems text adapter
//        new ClientSideTextAdapter(plugin).register(plugin);
//        logger.log("KuksoItems text adapter hooked.");

        // RealisticSeasons
        if (Bukkit.getPluginManager().isPluginEnabled("RealisticSeasons")) {
//            Bukkit.getPluginManager().registerEvents(new Listener(plugin), plugin);
            logger.log("RealisticSeasons listener registered.");
        } else {
            logger.log("RealisticSeasons not found. Skipped integration.");
        }
    }
}
