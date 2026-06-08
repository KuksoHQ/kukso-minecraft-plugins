package com.kukso.minecraft.lib.services;

import com.kukso.minecraft.lib.modules.logger.LoggingManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServiceRegistrar {
    private ServiceRegistrar() { /* no-op */ }
    public static void registerAll(JavaPlugin plugin,
                                   KuksoAPI api,
                                   LoggingManager loggingManager) {
        ServicesManager sm = plugin.getServer().getServicesManager();

        sm.register(LoggingManager.class, loggingManager, plugin, ServicePriority.Normal);
        plugin.getLogger().info("LoggingManager registered.");

        sm.register(KuksoAPI.class, api, plugin, ServicePriority.Normal);
        KuksoAPIProvider.register(api);
        plugin.getLogger().info("KuksoAPI registered.");
    }
}
