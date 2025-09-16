package io.github.devbd1.CubDialogs.API.addon;

/**
 * Core interface for CubDialogs addons.
 * Addons are discovered via Java's ServiceLoader mechanism.
 */
public interface CubDialogsAddon {
    /**
     * @return Unique identifier for this addon
     */
    String id();
    
    /**
     * @return Version of this addon
     */
    String version();
    
    /**
     * Called when the addon is being enabled.
     * This is where you should register dialog action listeners.
     *
     * @param context The addon context providing access to resources
     * @throws Exception If initialization fails
     */
    void onEnable(AddonContext context) throws Exception;
    
    /**
     * Called when the addon is being disabled.
     * Clean up any resources and unregister listeners here.
     *
     * @throws Exception If cleanup fails
     */
    void onDisable() throws Exception;
}
