package com.kukso.minecraft.votes.config;

import com.kukso.minecraft.votes.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Minimal, self-contained localization for KuksoVotes.
 *
 * <p>Because KuksoLib is only a soft dependency, KuksoVotes ships its own small
 * language loader and legacy color handling so it works fully standalone. When
 * richer shared localization from KuksoLib is wired in later, this remains the
 * fallback used whenever KuksoLib is absent.
 */
public class LangManager {

    private static final String DEFAULT_LOCALE = "en";
    private static final String[] BUNDLED_LOCALES = {"en", "tr"};

    private final Main plugin;
    private FileConfiguration messages;
    private String prefix = "";

    public LangManager(Main plugin) {
        this.plugin = plugin;
        reload();
    }

    /** Re-saves any missing bundled locales and reloads the configured language. */
    public void reload() {
        saveBundledLocales();

        String locale = plugin.getConfig().getString("language", DEFAULT_LOCALE);
        File file = new File(plugin.getDataFolder(), "lang/" + locale + ".yml");
        if (!file.exists()) {
            file = new File(plugin.getDataFolder(), "lang/" + DEFAULT_LOCALE + ".yml");
        }
        this.messages = YamlConfiguration.loadConfiguration(file);
        this.prefix = colorize(messages.getString("prefix", ""));
    }

    private void saveBundledLocales() {
        for (String locale : BUNDLED_LOCALES) {
            String resourcePath = "lang/" + locale + ".yml";
            if (!new File(plugin.getDataFolder(), resourcePath).exists()) {
                plugin.saveResource(resourcePath, false);
            }
        }
    }

    /** Raw (uncolored) message for {@code key}, falling back to the key itself. */
    public String raw(String key) {
        return messages.getString(key, key);
    }

    /** Translates legacy {@code &} color codes. */
    public String colorize(String text) {
        return text == null ? "" : ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Colorized message for {@code key} with {@code <name>} placeholder
     * replacements supplied as alternating name/value pairs.
     */
    public String message(String key, String... replacements) {
        String value = raw(key);
        for (int i = 0; i + 1 < replacements.length; i += 2) {
            value = value.replace("<" + replacements[i] + ">", replacements[i + 1]);
        }
        return colorize(value);
    }

    /** Same as {@link #message} but prefixed with the plugin chat prefix. */
    public String prefixed(String key, String... replacements) {
        return prefix + message(key, replacements);
    }
}
