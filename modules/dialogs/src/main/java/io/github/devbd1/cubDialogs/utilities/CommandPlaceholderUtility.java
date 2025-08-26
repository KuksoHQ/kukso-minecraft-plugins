// Java
package io.github.devbd1.cubDialogs.utilities;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public final class CommandPlaceholderUtility {
    private CommandPlaceholderUtility() {}

    private static volatile Boolean PAPI_AVAILABLE = null;

    private static boolean hasPapi() {
        if (PAPI_AVAILABLE != null) return PAPI_AVAILABLE;
        boolean available;
        try {
            Class.forName("me.clip.placeholderapi.PlaceholderAPI", false, CommandPlaceholderUtility.class.getClassLoader());
            available = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        } catch (ClassNotFoundException e) {
            available = false;
        }
        PAPI_AVAILABLE = available;
        // Bukkit.getLogger().info("[CubDialogs][PAPI] Detected: " + available);
        return available;
    }

    private static String applyPapi(String input, Player player) {
        try {
            Class<?> papiClass = Class.forName("me.clip.placeholderapi.PlaceholderAPI");
            Method setPlaceholders = papiClass.getMethod("setPlaceholders", Player.class, String.class);
            // String before = input;
            Object out = setPlaceholders.invoke(null, player, input);
            String after = out instanceof String s ? s : input;
            // Bukkit.getLogger().info("[CubDialogs][PAPI] Before: " + before);
            // Bukkit.getLogger().info("[CubDialogs][PAPI] After:  " + after);
            return after;
        } catch (Throwable t) {
            // Keep warnings for real failures
            // Bukkit.getLogger().warning("[CubDialogs][PAPI] Expansion failed: " + t.getClass().getSimpleName() + " - " + t.getMessage());
            return input;
        }
    }

    private static String applyBuiltinPapiFallbacks(String input, Player p) {
        // String before = input;
        String out = input
            .replace("%player_name%", p.getName())
            .replace("%player_uuid%", p.getUniqueId().toString())
            .replace("%player_world%", p.getWorld().getName())
            .replace("%world%", p.getWorld().getName())
            .replace("%player_gamemode%", p.getGameMode().name());

        // if (!before.equals(out)) {
        //     Bukkit.getLogger().info("[CubDialogs][PAPI-Fallback] Applied built-in replacements:");
        //     Bukkit.getLogger().info("[CubDialogs][PAPI-Fallback] In:  " + before);
        //     Bukkit.getLogger().info("[CubDialogs][PAPI-Fallback] Out: " + out);
        // }
        return out;
    }

    public static String resolveCommandPlaceholders(String command,
                                                    Audience audience,
                                                    Configuration dialogRoot) {
        if (command == null) return "";

        // String original = command;
        String result = command;

        final boolean isPlayer = audience instanceof Player;
        final String playerName = isPlayer ? ((Player) audience).getName() : "CONSOLE";

        result = result.replace("<player>", playerName);
        if (isPlayer) {
            Player p = (Player) audience;
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

        // if (!original.equals(result)) {
        //     Bukkit.getLogger().info("[CubDialogs][Placeholders] Native replaced:");
        //     Bukkit.getLogger().info("[CubDialogs][Placeholders] In:  " + original);
        //     Bukkit.getLogger().info("[CubDialogs][Placeholders] Out: " + result);
        // }

        if (isPlayer && result.contains("%") && hasPapi()) {
            result = applyPapi(result, (Player) audience);
        } else if (result.contains("%")) {
            // Bukkit.getLogger().info("[CubDialogs][PAPI] Skipped (no PAPI or no player context). Command: " + result);
        }

        if (isPlayer && result.contains("%")) {
            result = applyBuiltinPapiFallbacks(result, (Player) audience);
        }

        return result;
    }
}
