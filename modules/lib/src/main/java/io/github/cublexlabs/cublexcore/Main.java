package io.github.cublexlabs.cublexcore;

import io.github.cublexlabs.cublexcore.commands.CommandRegistrar;
import io.github.cublexlabs.cublexcore.hooks.HookRegistrar;
import io.github.cublexlabs.cublexcore.modules.ModuleRegistrar;
import io.github.cublexlabs.cublexcore.services.ServiceRegistrar;
import io.github.cublexlabs.cublexcore.services.ServiceUnregistrar;

import io.github.cublexlabs.cublexcore.modules.logger.LoggingManager;
import io.github.cublexlabs.cublexcore.modules.text.Lang;
import io.github.cublexlabs.cublexcore.services.CorlexAPIImplementer;
import io.github.cublexlabs.cublexcore.utilities.*;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private LoggingManager logger;
    private CorlexAPIImplementer api;

    @Override
    public void onEnable() {
        // --- config & localization
        saveDefaultConfig();
        ConfigManager.getInstance().init(this);
        Lang.load(this);
        ConfigManager.getInstance().printStatus(this);

        // --- core services
        logger = new LoggingManager(this);
        api    = new CorlexAPIImplementer(this, logger);
        ServiceRegistrar.registerAll(this, api, logger);

        // --- commands
        CommandRegistrar.register(this, logger);

        // --- hooks (PlaceholderAPI, CubItems, RealisticSeasons…)
        HookRegistrar.register(this, logger);

        // ** ProtocolLib packet‐translation hook **
        ModuleRegistrar.register(this, api, logger);

        logger.log("CublexCore is fully enabled.");
    }

    @Override
    public void onDisable() {
        ServiceUnregistrar.unregisterAll(this, api, logger);
        logger.log("CublexCore is disabled.");
    }

    public LoggingManager getLoggingManager() {
        return logger;
    }
}