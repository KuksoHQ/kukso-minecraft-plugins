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
        CubDialogsAPI.bootstrap(actionRegistry, getDescription().getVersion());

        // Create and register the bridge
        actionBridge = new DialogActionBridge(this);
        getServer().getPluginManager().registerEvents(actionBridge, this);

        getLogger().info("CubDialogs API initialized with version " + CubDialogsAPI.getApiVersion());
    }

    private void initializeAddonSystem() {
        // Create the addon manager
        addonManager = new AddonManager(this, CubDialogsAPI.getApiVersion());

        // Create addons directory if it doesn't exist
        File addonsDir = new File(getDataFolder(), "addons");
        if (!addonsDir.exists() && !addonsDir.mkdirs()) {
            getLogger().warning("Failed to create addons directory");
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
}
