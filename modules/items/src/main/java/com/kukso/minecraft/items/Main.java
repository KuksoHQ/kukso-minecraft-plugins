package com.kukso.minecraft.items;

import com.kukso.minecraft.items.command.GiveItemCommand;
import com.kukso.minecraft.items.item.ItemManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    private static Main instance;

    private FileConfiguration itemsConfig;

    @Override
    public void onEnable() {
        instance = this;

        // Load config.yml
        saveDefaultConfig();

        // Load items.yml
        loadItemsConfig();

        // Load custom items
        ItemManager.loadItems(getItemsConfig());

        getLogger().info("KuksoItems has been enabled.");

        getCommand("giveitem").setExecutor(new GiveItemCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("KuksoItems has been disabled.");
    }

    private void loadItemsConfig() {
        File file = new File(getDataFolder(), "items.yml");
        if (!file.exists()) {
            saveResource("items.yml", false);
        }
        itemsConfig = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getItemsConfig() {
        return itemsConfig;
    }

    public static Main getInstance() {
        return instance;
    }

}
