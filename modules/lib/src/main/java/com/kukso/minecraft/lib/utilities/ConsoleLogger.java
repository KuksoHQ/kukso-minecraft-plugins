package com.kukso.minecraft.lib.utilities;

import org.bukkit.plugin.java.JavaPlugin;

public class ConsoleLogger {
    public void PrintConsole(JavaPlugin plugin, String type, String message) {
        switch (type) {
            case "warning":
                plugin.getLogger().warning(message);
                break;
            case "info":
                plugin.getLogger().info(message);
                break;
            case "severe":
                plugin.getLogger().severe(message);
                break;
        }
    }
    public void TestPrint(JavaPlugin plugin, String key, String def) {
        PrintConsole(plugin, "warning", "Missing/empty '" + key + "', using default: " + def);
    }
}
