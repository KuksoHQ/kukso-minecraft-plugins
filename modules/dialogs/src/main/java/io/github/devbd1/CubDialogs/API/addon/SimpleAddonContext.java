package io.github.devbd1.CubDialogs.API.addon;

import io.github.devbd1.CubDialogs.API.addon.AddonContext;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

/**
 * Simple implementation of AddonContext.
 */
class SimpleAddonContext implements AddonContext {
    private final Plugin hostPlugin;
    private final Logger addonLogger;
    private final String apiVersion;
    
    SimpleAddonContext(Plugin hostPlugin, String addonId, String apiVersion) {
        this.hostPlugin = hostPlugin;
        this.addonLogger = new AddonLogger(hostPlugin.getLogger(), addonId);
        this.apiVersion = apiVersion;
    }
    
    @Override
    public Plugin hostPlugin() {
        return hostPlugin;
    }
    
    @Override
    public Server server() {
        return hostPlugin.getServer();
    }
    
    @Override
    public Logger logger() {
        return addonLogger;
    }
    
    @Override
    public String apiVersion() {
        return apiVersion;
    }
    
    /**
     * Simple logger wrapper that prepends the addon ID to messages.
     */
    private static class AddonLogger extends Logger {
        private final Logger parent;
        private final String prefix;
        
        AddonLogger(Logger parent, String addonId) {
            super(parent.getName() + "." + addonId, null);
            this.parent = parent;
            this.prefix = "[" + addonId + "] ";
            
            // Copy parent logger settings
            setLevel(parent.getLevel());
            setUseParentHandlers(parent.getUseParentHandlers());
        }
        
        @Override
        public void info(String msg) {
            parent.info(prefix + msg);
        }
        
        @Override
        public void warning(String msg) {
            parent.warning(prefix + msg);
        }
        
        @Override
        public void severe(String msg) {
            parent.severe(prefix + msg);
        }
    }
}
