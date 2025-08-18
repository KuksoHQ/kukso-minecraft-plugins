package io.github.devbd1.cubDialogs;

import io.github.devbd1.cubDialogs.commands.CmdRegistrar;
import io.github.devbd1.cubDialogs.listeners.DialogEventListener;
import io.github.devbd1.cubDialogs.program.DialogConfigManager;
import io.github.devbd1.cubDialogs.program.ServerLinksManager;
import io.github.devbd1.cubDialogs.utilities.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    
    private static Main instance;

    private ServerLinksManager serverLinksManager;
    
    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        try {
            // Initialize configuration managers
            ConfigManager.init(this);
            DialogConfigManager.init(this);
            // --- commands
            CmdRegistrar.register(this);

            // Register event listeners
            getServer().getPluginManager().registerEvents(new DialogEventListener(), this);

            // Initialize ServerLinksManager (this sets the static instance)
            serverLinksManager = new ServerLinksManager(this);
            serverLinksManager.setupServerLinks();
        
            getLogger().info("Plugin enabled successfully");
        } catch (Exception e) {
            getLogger().severe("Failed to enable plugin: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    @Override
    public void onDisable() {
        instance = null;
        // Plugin shutdown logic
    }
}
