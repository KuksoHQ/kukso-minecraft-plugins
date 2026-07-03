/**
 * Utility to unregister all KuksoLib services in reverse order.
 * Call this in onDisable() to cleanly remove your plugin’s service registrations.
 */
package com.kukso.minecraft.lib.services;

import com.kukso.minecraft.lib.modules.logger.LoggingManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServiceUnregistrar {
    private ServiceUnregistrar() { /* prevent instantiation */ }

    /**
     * @param plugin          your main JavaPlugin instance
     * @param api             the KuksoAPI implementation you registered
     * @param loggingManager  the LoggingManager you registered
     */
    public static void unregisterAll(JavaPlugin plugin,
                                     KuksoAPI api,
                                     LoggingManager loggingManager) {
        ServicesManager sm = plugin.getServer().getServicesManager();

        // 1) Unregister the core API
        sm.unregister(KuksoAPI.class, api);
        KuksoAPIProvider.unregister(api);
        plugin.getLogger().info("KuksoAPI unregistered.");

        // 2) Unregister the logging facility
        sm.unregister(LoggingManager.class, loggingManager);
        plugin.getLogger().info("LoggingManager unregistered.");
    }
}
