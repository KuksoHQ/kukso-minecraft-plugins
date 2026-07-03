package com.kukso.minecraft.lib;

import com.kukso.minecraft.lib.commands.CmdRegistrar;
import com.kukso.minecraft.lib.hooks.HookRegistrar;
import com.kukso.minecraft.lib.modules.ModuleRegistrar;
import com.kukso.minecraft.lib.services.ServiceRegistrar;
import com.kukso.minecraft.lib.services.ServiceUnregistrar;

import com.kukso.minecraft.lib.modules.logger.LoggingManager;
import com.kukso.minecraft.lib.modules.text.Lang;
import com.kukso.minecraft.lib.services.KuksoAPIImplementer;
import com.kukso.minecraft.lib.utilities.*;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private LoggingManager logger;
    private KuksoAPIImplementer api;

    @Override
    public void onEnable() {
        // --- config & localization
        saveDefaultConfig();
        ConfigManager.getInstance().init(this);
        Lang.load(this);
        ConfigManager.getInstance().printStatus(this);

        // --- core services
        logger = new LoggingManager(this);
        api    = new KuksoAPIImplementer(this, logger);
        ServiceRegistrar.registerAll(this, api, logger);

        // --- commands
        CmdRegistrar.register(this, logger);

        // --- hooks (PlaceholderAPI, KuksoItems, RealisticSeasons…)
        HookRegistrar.register(this, logger);

        // --- ProtocolLib packet-translation hook
        ModuleRegistrar.register(this, api, logger);

        logger.log("KuksoLib is fully enabled.");
    }

    @Override
    public void onDisable() {
        ServiceUnregistrar.unregisterAll(this, api, logger);
        logger.log("KuksoLib is disabled.");
    }

    public LoggingManager getLoggingManager() {
        return logger;
    }
}