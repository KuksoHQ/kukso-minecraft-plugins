
package io.github.devbd1.CubDialogs.API.addon;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

/**
 * Context provided to addons when they're enabled.
 * Gives access to necessary server resources without exposing implementation details.
 */
public interface AddonContext {
    /**
     * @return The host plugin (CubDialogs)
     */
    Plugin hostPlugin();

    /**
     * @return The Bukkit server instance
     */
    Server server();

    /**
     * @return A logger that prepends the addon's ID to messages
     */
    Logger logger();

    /**
     * @return The API version that this addon is running with
     */
    String apiVersion();
}