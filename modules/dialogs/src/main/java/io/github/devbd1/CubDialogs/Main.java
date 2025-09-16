package io.github.devbd1.CubDialogs;

import io.github.devbd1.CubDialogs.commands.CmdRegistrar;
import io.github.devbd1.CubDialogs.API.CubDialogsAPI;
import io.github.devbd1.CubDialogs.API.addon.AddonManager;
import io.github.devbd1.CubDialogs.commands.CmdRegistrar;
import io.github.devbd1.CubDialogs.dialog.DialogActionBridge;
import io.github.devbd1.CubDialogs.dialog.DialogConfigManager;
import io.github.devbd1.CubDialogs.dialog.SimpleDialogActionRegistry;
import io.github.devbd1.CubDialogs.serverLinks.ServerLinksManager;
import io.github.devbd1.CubDialogs.utilities.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class Main extends JavaPlugin {

    private static Main instance;
    private ServerLinksManager serverLinksManager;
    private AddonManager addonManager;
    private SimpleDialogActionRegistry actionRegistry;
    private DialogActionBridge actionBridge;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        //if (!isCublexCorePresent()) {
            //getLogger().severe("CublexCore dependency is missing or not enabled. Disabling CubDialogs.");
            //getServer().getPluginManager().disablePlugin(this);
        //return;
        //}

        try {
            // Initialize configuration managers
            ConfigManager.init(this);
            DialogConfigManager.init(this);

            // Initialize the API
            initializeAPI();

            // Register commands
            CmdRegistrar.register(this);

            // Initialize addon system
            initializeAddonSystem();

            // Initialize ServerLinksManager
            serverLinksManager = new ServerLinksManager(this);
            serverLinksManager.setupServerLinks();

            getLogger().info("Plugin enabled successfully");
        } catch (Exception e) {
            getLogger().severe("Failed to enable plugin: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Shutdown addons
        if (addonManager != null) {
            addonManager.unloadAddons();
        }

        // Shutdown API
        CubDialogsAPI.shutdown();

        instance = null;
    }

    private boolean isCublexCorePresent() {
        var pm = getServer().getPluginManager();
        var plugin = pm.getPlugin("Cub");
        return plugin != null && plugin.isEnabled();
    }

    private void initializeAPI() {
        // Create the registry
        actionRegistry = new SimpleDialogActionRegistry(getLogger());

        // Bootstrap the API with this registry and our plugin version
        CubDialogsAPI.bootstrap(actionRegistry, "1.0.0");

        // Create and register the bridge
        actionBridge = new DialogActionBridge(this);
        getServer().getPluginManager().registerEvents(actionBridge, this);

        getLogger().info("CubDialogs API initialized with version " + CubDialogsAPI.getApiVersion());
    }

    private void initializeAddonSystem() {
        // Create the addon manager
        addonManager = new AddonManager(this, CubDialogsAPI.getApiVersion());

        File addonsDir = new File(getDataFolder(), "addons");
        if (!addonsDir.exists() && !addonsDir.mkdirs()) {
            getLogger().warning("Failed to create addons directory");
        } else {
            // Copy addons from resources if the directory is empty or if files are missing
            copyAddonsFromResources(addonsDir);
        }

        // Load addons
        addonManager.loadAddons();
    }

    /**
     * Gets the addon manager instance.
     */
    public AddonManager getAddonManager() {
        return addonManager;
    }

    /**
     * Copies addon files from the plugin's resources to the addons directory
     * if they don't already exist.
     *
     * @param addonsDir The target addons directory
     */
    private void copyAddonsFromResources(File addonsDir) {
        try {
            // Check if the resource directory exists
            JarFile jarFile = new JarFile(getFile());
            Enumeration<JarEntry> entries = jarFile.entries();
            boolean foundAddons = false;

            // First, collect all addon files from resources
            List<String> resourceAddons = new ArrayList<>();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.startsWith("addons/") && name.endsWith(".jar") && !entry.isDirectory()) {
                    foundAddons = true;
                    resourceAddons.add(name);
                }
            }

            if (!foundAddons) {
                getLogger().info("No addon files found in resources.");
                jarFile.close();
                return;
            }

            // Copy each addon file if it doesn't exist in the target directory
            for (String resourcePath : resourceAddons) {
                String fileName = resourcePath.substring(resourcePath.lastIndexOf('/') + 1);
                File targetFile = new File(addonsDir, fileName);

                if (!targetFile.exists()) {
                    getLogger().info("Copying addon from resources: " + fileName);
                    try (InputStream in = getResource(resourcePath);
                         FileOutputStream out = new FileOutputStream(targetFile)) {

                        if (in == null) {
                            getLogger().warning("Could not find resource: " + resourcePath);
                            continue;
                        }

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = in.read(buffer)) > 0) {
                            out.write(buffer, 0, length);
                        }
                        getLogger().info("Successfully copied addon: " + fileName);
                    } catch (IOException e) {
                        getLogger().warning("Failed to copy addon " + fileName + ": " + e.getMessage());
                    }
                } else {
                    getLogger().info("Addon already exists: " + fileName);
                }
            }

            jarFile.close();
        } catch (IOException e) {
            getLogger().warning("Error copying addons from resources: " + e.getMessage());
        }
    }

}
