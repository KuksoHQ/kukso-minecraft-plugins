/**
 * Singleton helper to manage the configuration.
 * â€¢ Call init(plugin) inside onEnable().
 * â€¢ Copies â€œlang/{fallback}.ymlâ€ from bundled resources if missing.
 * â€¢ Provides getString/getBoolean/getInt/getDouble/getList for safe reads with defaults.
 * â€¢ printStatus() logs current config settings to console.
 */
package com.kukso.minecraft.lib.utilities;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ConfigManager {
    private static ConfigManager instance;
    private FileConfiguration config;
    private static final List<String> SUPPORTED_LANGS = List.of("en", "tr");
    private JavaPlugin plugin;

    private ConfigManager() { }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * Initializes the ConfigManager. Must be called from onEnable().
     */
    public void init(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        // copy *all* bundled langs
        copyLanguageFiles(plugin);

        config = plugin.getConfig();
//        loadLanguageFile(getFallbackLanguage());
    }

    /** Ensure each lang/<code>.yml exists on disk */
    private void copyLanguageFiles(JavaPlugin plugin) {
        Path langDir = plugin.getDataFolder().toPath().resolve("lang");
        try {
            Files.createDirectories(langDir);
            for (String lang : SUPPORTED_LANGS) {
                Path file = langDir.resolve(lang + ".yml");
                if (Files.notExists(file)) {
                    String resourcePath = "lang/" + lang + ".yml";
                    if (plugin.getResource(resourcePath) != null) {
                        plugin.saveResource(resourcePath, false);
                        plugin.getLogger().info("Copied language file: " + lang + ".yml");
                    } else {
                        plugin.getLogger().warning("Missing bundled resource: " + resourcePath);
                    }
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Could not create lang directory: " + e.getMessage());
        }
    }

    /**
     * Ensures the language file exists under plugins/{plugin}/lang,
     * copying it from the JAR if necessary.
     */
    private void loadLanguageFile(JavaPlugin plugin, String lang) {
        Path langDir  = plugin.getDataFolder().toPath().resolve("lang");
        Path langFile = langDir.resolve(lang + ".yml");
        try {
            Files.createDirectories(langDir);
            if (Files.notExists(langFile)) {
                plugin.saveResource("lang/" + lang + ".yml", false);
                plugin.getLogger().info("Generated lang/" + lang + ".yml from defaults.");
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to ensure language file: " + e.getMessage());
        }
    }

    /**
     * Logs the current configuration status to console.
     */
    public void printStatus(JavaPlugin plugin) {
        plugin.getLogger().info("Config Status:");
        plugin.getLogger().info(" - Server Name: "     + getString("server-name",       "Kukso.net"));
        plugin.getLogger().info(" - Default Language: " + getFallbackLanguage());
        plugin.getLogger().info(" - Logging Enabled: "  + getBoolean("logging-enabled",   false));
        plugin.getLogger().info(" - REST API Enabled: " + getBoolean("restful-enabled",   false));
        plugin.getLogger().info(" - Discord Webhook: "  + getString("discord",            ""));
        plugin.getLogger().info(" - Telegram Bot Key: " + getString("telegram",           ""));

        // List all loaded locale files
        try {
            Files.list(plugin.getDataFolder().toPath().resolve("lang"))
                    .filter(p -> p.toString().endsWith(".yml"))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .forEach(f -> plugin.getLogger().info(" - Loaded locale: " + f));
        } catch (IOException e) {
            plugin.getLogger().warning("Could not list locale files: " + e.getMessage());
        }
    }

    /**
     * Validates that the config key is non-null and matches allowed characters.
     */
    private void validateKey(String key) {
        if (key == null || !key.matches("[\\w.-]+")) {
            throw new IllegalArgumentException("Invalid config key: " + key);
        }
    }

    /**
     * Reads a string value or returns the default if missing/empty.
     */
    public String getString(String key, String def) {
        validateKey(key);
        String v = config.getString(key);
        if (v == null || v.isEmpty()) {
            plugin.getLogger().warning("Missing/empty '" + key + "', using default: " + def);
            return def;
        }
        return v;
    }

    /**
     * Reads a boolean value or returns the default if missing.
     */
    public boolean getBoolean(String key, boolean def) {
        validateKey(key);
        if (!config.contains(key)) {
            plugin.getLogger().warning("Missing '" + key + "', using default: " + def);
            return def;
        }
        return config.getBoolean(key);
    }

    /**
     * Reads an integer value or returns the default if missing.
     */
    public int getInt(String key, int def) {
        validateKey(key);
        if (!config.contains(key)) {
            plugin.getLogger().warning("Missing '" + key + "', using default: " + def);
            return def;
        }
        return config.getInt(key);
    }

    /**
     * Reads a double value or returns the default if missing.
     */
    public double getDouble(String key, double def) {
        validateKey(key);
        if (!config.contains(key)) {
            plugin.getLogger().warning("Missing '" + key + "', using default: " + def);
            return def;
        }
        return config.getDouble(key);
    }

    /**
     * Reads a List<T> or returns the default list if missing.
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, List<T> def) {
        validateKey(key);
        List<T> v = (List<T>) config.getList(key);
        if (v == null) {
            plugin.getLogger().warning("Missing '" + key + "', using default list.");
            return def;
        }
        return v;
    }

    /**
     * Returns all top-level string entries as placeholders.
     */
    public Map<String,String> getAllAsPlaceholders() {
        Map<String,String> m = new HashMap<>();
        config.getKeys(false).forEach(k -> {
            String v = config.getString(k);
            if (v != null) m.put(k, v);
        });
        return m;
    }

    /** Helper to fetch and lowercase the fallback language. */
    private String getFallbackLanguage() {

        return getString("fallback-language", "en").toLowerCase();
    }

    /**
     * Returns the underlying FileConfiguration object.
     * @return The current configuration
     */
    public FileConfiguration getConfig() {
        if (config == null) {
            throw new IllegalStateException("ConfigManager has not been initialized. Call init() first.");
        }
        return config;
    }
}