package io.github.devbd1.cubDialogs;

import io.github.devbd1.cubDialogs.commands.CmdRegistrar;
import io.github.devbd1.cubDialogs.listeners.DialogEventListener;
import io.github.devbd1.cubDialogs.program.DialogConfigManager;
import io.github.devbd1.cubDialogs.utilities.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            // Initialize configuration managers
            ConfigManager.init(this);
            DialogConfigManager.init(this);
            // --- commands
            CmdRegistrar.register(this);


            // Register event listeners
            getServer().getPluginManager().registerEvents(new DialogEventListener(), this);
        
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
        // Plugin shutdown logic
    }
}
