package com.kukso.minecraft.votes;

import com.kukso.minecraft.votes.commands.RootCommand;
import com.kukso.minecraft.votes.config.LangManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * KuksoVotes bootstrap.
 *
 * <p>KuksoVotes is a vote-site-agnostic voting-rewards plugin. KuksoLib is only a
 * soft dependency: the plugin runs fully standalone and treats KuksoLib as an
 * optional enhancement that is detected at runtime. This class is currently a
 * minimal scaffold — vote-protocol handling and reward distribution are tracked
 * in the repository backlog and added in later phases.
 */
public class Main extends JavaPlugin {

    private static Main instance;

    private LangManager lang;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        this.lang = new LangManager(this);

        getLogger().info(isKuksoLibPresent()
                ? "KuksoLib detected — ecosystem integration is available."
                : "Running standalone (KuksoLib is not installed).");

        getCommand("kuksovotes").setExecutor(new RootCommand(this));

        getLogger().info("KuksoVotes has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("KuksoVotes has been disabled.");
    }

    /**
     * Whether KuksoLib is present at runtime. KuksoLib is a soft dependency, so
     * every use of its services must be guarded by this check.
     */
    public boolean isKuksoLibPresent() {
        return getServer().getPluginManager().getPlugin("KuksoLib") != null;
    }

    public LangManager lang() {
        return lang;
    }

    public static Main getInstance() {
        return instance;
    }
}
