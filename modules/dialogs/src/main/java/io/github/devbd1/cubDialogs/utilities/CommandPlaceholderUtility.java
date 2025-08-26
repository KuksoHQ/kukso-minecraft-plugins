// Java
package io.github.devbd1.cubDialogs.utilities;

import net.kyori.adventure.audience.Audience;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

public final class CommandPlaceholderUtility {
    private CommandPlaceholderUtility() {}

    // Replaces placeholders only; does NOT strip any formatting or JSON characters.
    // Supported:
    //   <player>   -> viewing player's name (or "CONSOLE")
    //   <uuid>     -> viewing player's UUID (if a player)
    //   <world>    -> player's world name
    //   <x>/<y>/<z>-> player's block coordinates
    //   <pos>      -> "x y z" (block coordinates)
    //   <gamemode> -> player's game mode name (e.g., SURVIVAL)
    public static String resolveCommandPlaceholders(String command,
                                                    Audience audience,
                                                    Configuration dialogRoot) {
        if (command == null) return "";

        String result = command;

        // Always available
        final boolean isPlayer = audience instanceof Player;
        final String playerName = isPlayer ? ((Player) audience).getName() : "CONSOLE";
        result = result.replace("<player>", playerName);

        if (isPlayer) {
            Player p = (Player) audience;

            // Player-specific placeholders
            result = result.replace("<uuid>", p.getUniqueId().toString());
            result = result.replace("<world>", p.getWorld().getName());

            int bx = p.getLocation().getBlockX();
            int by = p.getLocation().getBlockY();
            int bz = p.getLocation().getBlockZ();
            result = result.replace("<x>", Integer.toString(bx));
            result = result.replace("<y>", Integer.toString(by));
            result = result.replace("<z>", Integer.toString(bz));
            result = result.replace("<pos>", bx + " " + by + " " + bz);

            result = result.replace("<gamemode>", p.getGameMode().name());
        }

        return result;
    }
}
