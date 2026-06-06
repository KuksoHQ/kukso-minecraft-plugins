package io.github.devbd1.CubDialogs.API.addon;

import io.github.devbd1.CubDialogs.API.addon.AddonContext;
import io.github.devbd1.CubDialogs.API.addon.CubDialogsAddon;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the discovery, loading, and lifecycle of CubDialogs addons.
 */
public class AddonManager {
    private final Plugin plugin;
    private final Logger logger;
    private final File addonsDir;
    private final Map<String, LoadedAddon> loadedAddons = new ConcurrentHashMap<>();
    private final String apiVersion;

    public AddonManager(Plugin plugin, String apiVersion) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.apiVersion = apiVersion;
        
        // Ensure the addons directory exists
        this.addonsDir = new File(plugin.getDataFolder(), "addons");
        if (!addonsDir.exists() && !addonsDir.mkdirs()) {
            logger.warning("Failed to create addons directory");
        }
    }
    
    /**
     * Loads all addons from the addons directory.
     */
    public void loadAddons() {
        File[] jarFiles = addonsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar"));
        
        if (jarFiles == null || jarFiles.length == 0) {
            logger.info("No addon JARs found in " + addonsDir.getAbsolutePath());
            return;
        }
        
        logger.info("Found " + jarFiles.length + " potential addon JAR(s)");
        
        for (File jarFile : jarFiles) {
            try {
                loadAddon(jarFile);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Failed to load addon from " + jarFile.getName(), e);
            }
        }
        
        logger.info("Loaded " + loadedAddons.size() + " addon(s)");
    }
    
    /**
     * Unloads all addons.
     */
    public void unloadAddons() {
        for (LoadedAddon addon : new ArrayList<>(loadedAddons.values())) {
            try {
                unloadAddon(addon.id);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error unloading addon: " + addon.id, e);
            }
        }
        loadedAddons.clear();
    }
    
    /**
     * Loads an addon from a JAR file.
     */
    private void loadAddon(File jarFile) throws Exception {
        logger.info("Loading addon from " + jarFile.getName());
        
        // Create a URL class loader for this JAR
        URL jarUrl = jarFile.toURI().toURL();
        URLClassLoader classLoader = new URLClassLoader(
                new URL[]{jarUrl},
                getClass().getClassLoader()
        );
        
        // Use ServiceLoader to find CubDialogsAddon implementations
        ServiceLoader<CubDialogsAddon> serviceLoader = ServiceLoader.load(
                CubDialogsAddon.class, 
                classLoader
        );
        
        List<CubDialogsAddon> discoveredAddons = new ArrayList<>();
        for (CubDialogsAddon addon : serviceLoader) {
            discoveredAddons.add(addon);
        }
        
        if (discoveredAddons.isEmpty()) {
            logger.warning("No CubDialogsAddon implementation found in " + jarFile.getName());
            classLoader.close();
            return;
        }
        
        if (discoveredAddons.size() > 1) {
            logger.warning("Multiple CubDialogsAddon implementations found in " + 
                    jarFile.getName() + ". Using the first one.");
        }
        
        CubDialogsAddon addonInstance = discoveredAddons.get(0);
        String addonId = addonInstance.id();
        
        // Check if this addon ID is already loaded
        if (loadedAddons.containsKey(addonId)) {
            logger.warning("Addon with ID '" + addonId + "' is already loaded. Skipping " + 
                    jarFile.getName());
            classLoader.close();
            return;
        }
        
        // Create addon context
        AddonContext context = new io.github.devbd1.CubDialogs.API.addon.SimpleAddonContext(
                plugin,
                addonId,
                apiVersion
        );
        
        // Enable the addon
        try {
            addonInstance.onEnable(context);
            
            // Store the loaded addon
            LoadedAddon loadedAddon = new LoadedAddon(
                    addonId,
                    addonInstance,
                    classLoader,
                    jarFile.getAbsolutePath()
            );
            loadedAddons.put(addonId, loadedAddon);
            
            logger.info("Addon '" + addonId + "' v" + addonInstance.version() + " enabled successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to enable addon '" + addonId + "'", e);
            classLoader.close();
            throw e;
        }
    }
    
    /**
     * Unloads a specific addon by ID.
     */
    private void unloadAddon(String addonId) throws Exception {
        LoadedAddon loadedAddon = loadedAddons.remove(addonId);
        if (loadedAddon == null) {
            logger.warning("No addon with ID '" + addonId + "' is loaded");
            return;
        }
        
        try {
            loadedAddon.instance.onDisable();
            logger.info("Addon '" + addonId + "' disabled successfully");
        } finally {
            // Always try to close the class loader
            loadedAddon.classLoader.close();
        }
    }
    
    /**
     * Gets a list of all loaded addon IDs.
     */
    public List<String> getLoadedAddonIds() {
        return new ArrayList<>(loadedAddons.keySet());
    }
    
    /**
     * Gets info about all loaded addons.
     */
    public List<AddonInfo> getLoadedAddonInfo() {
        List<AddonInfo> result = new ArrayList<>();
        for (LoadedAddon addon : loadedAddons.values()) {
            result.add(new AddonInfo(
                    addon.id,
                    addon.instance.version(),
                    addon.jarPath
            ));
        }
        return result;
    }
    
    /**
     * Internal class to hold loaded addon data.
     */
    private static class LoadedAddon {
        final String id;
        final CubDialogsAddon instance;
        final URLClassLoader classLoader;
        final String jarPath;
        
        LoadedAddon(String id, CubDialogsAddon instance, URLClassLoader classLoader, String jarPath) {
            this.id = id;
            this.instance = instance;
            this.classLoader = classLoader;
            this.jarPath = jarPath;
        }
    }
    
    /**
     * Public class for addon info.
     */
    public static class AddonInfo {
        private final String id;
        private final String version;
        private final String jarPath;
        
        public AddonInfo(String id, String version, String jarPath) {
            this.id = id;
            this.version = version;
            this.jarPath = jarPath;
        }
        
        public String getId() {
            return id;
        }
        
        public String getVersion() {
            return version;
        }
        
        public String getJarPath() {
            return jarPath;
        }
    }
}
