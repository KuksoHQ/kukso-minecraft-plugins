package io.github.devbd1.cubDialogs.features.serverLinks;

import io.github.devbd1.cubDialogs.utilities.ColorManager;
import io.github.devbd1.cubDialogs.utilities.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ServerLinks;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

/**
 * Example class demonstrating how to manage server links in PaperMC 1.21+
 * This feature allows servers to display links in the multiplayer menu
 */
@SuppressWarnings("deprecation")
public class ServerLinksManager {

    private final JavaPlugin plugin;
    private static ServerLinksManager instance;

    public ServerLinksManager(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;
    }

    /**
     * Sets up all server links when the plugin enables
     */
    public void setupServerLinks() {

        ServerLinks serverLinks = Bukkit.getServer().getServerLinks();

        try {
            // Add typed links (these use predefined ServerLinks.Type enum values)
            addTypedLinks(serverLinks);

            // Add custom links with display names
            addCustomLinks(serverLinks);

            plugin.getLogger().info("Server links have been configured successfully!");

        } catch (URISyntaxException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to configure server links due to invalid URI", e);
        }
    }

    /**
     * Static method to reload server links (for use in reload commands)
     */
    public static void reloadServerLinks() {
        if (instance != null) {
            instance.setupServerLinks();
        } else {
            // Log error if instance is not available
            if (Bukkit.getServer() != null) {
                Bukkit.getLogger().warning("ServerLinksManager instance not available for reload!");
            }
        }
    }

    /**
     * Adds links using the predefined ServerLinks.Type enum values from config
     */
    private void addTypedLinks(ServerLinks serverLinks) throws URISyntaxException {

        // Array of all supported server link types
        ServerLinks.Type[] linkTypes = {
                ServerLinks.Type.WEBSITE,
                ServerLinks.Type.COMMUNITY,
                ServerLinks.Type.FORUMS,
                ServerLinks.Type.SUPPORT,
                ServerLinks.Type.NEWS,
                ServerLinks.Type.ANNOUNCEMENTS,
                ServerLinks.Type.STATUS,
                ServerLinks.Type.FEEDBACK,
                ServerLinks.Type.REPORT_BUG,
                ServerLinks.Type.COMMUNITY_GUIDELINES
        };

        // Process each link type from config
        for (ServerLinks.Type type : linkTypes) {
            String configPath = "serverLinks." + type.name();

            // Check if this link type is configured
            if (ConfigManager.getConfig().contains(configPath + ".url")) {
                String url = ConfigManager.getString(configPath + ".url", null);
                //String label = ConfigManager.getString(configPath + ".label", type.name());

                if (url != null && !url.isEmpty()) {
                    try {
                        serverLinks.setLink(type, new URI(url));
                        plugin.getLogger().info("Added " + type.name() + " link: " + url);
                    } catch (URISyntaxException e) {
                        plugin.getLogger().warning("Invalid URL for " + type.name() + ": " + url);
                        throw e;
                    }
                } else {
                    plugin.getLogger().warning("Empty URL for " + type.name() + ", skipping...");
                }
            } else {
                plugin.getLogger().info("No configuration found for " + type.name() + ", skipping...");
            }
        }
    }

    /**
     * Adds custom links with custom display names from config using Adventure Components
     */
    private void addCustomLinks(ServerLinks serverLinks) throws URISyntaxException {
        // Import the ColorManager
        // Note: Add this import at the top of your class:
        // import io.github.devbd1.cubDialogs.utilities.ColorManager;

        // Check if customLinks section exists in config
        if (!ConfigManager.getConfig().contains("customLinks")) {
            plugin.getLogger().info("No custom links configured, skipping...");
            return;
        }

        // Get all custom link keys
        var customLinksSection = ConfigManager.getConfig().getConfigurationSection("customLinks");
        if (customLinksSection == null) {
            plugin.getLogger().warning("Custom links section is malformed, skipping...");
            return;
        }

        // Process each custom link
        for (String linkKey : customLinksSection.getKeys(false)) {
            String configPath = "customLinks." + linkKey;

            String url = ConfigManager.getString(configPath + ".url", null);
            String label = ConfigManager.getString(configPath + ".label", linkKey);

            if (url != null && !url.isEmpty()) {
                try {
                    // Apply color formatting using ColorManager
                    String coloredLabel = ColorManager.applyColorFormatting(label);

                    // Convert the colored string to Adventure Component
                    // This will preserve the Minecraft color codes and convert them properly
                    Component linkText = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
                            .legacySection()
                            .deserialize(coloredLabel);

                    serverLinks.addLink(linkText, new URI(url));
                    plugin.getLogger().info("Added custom link " + linkKey + ": " + url + " (" + label + ")");

                } catch (URISyntaxException e) {
                    plugin.getLogger().warning("Invalid URL for custom link " + linkKey + ": " + url);
                    throw e;
                }
            } else {
                plugin.getLogger().warning("Empty URL for custom link " + linkKey + ", skipping...");
            }
        }
    }

    /**
     * Updates an existing server link
     */
    public void updateLink(ServerLinks.Type type, String newUrl) {
        try {
            ServerLinks serverLinks = Bukkit.getServer().getServerLinks();
            serverLinks.setLink(type, new URI(newUrl));
            plugin.getLogger().info("Updated " + type.name() + " link to: " + newUrl);
        } catch (URISyntaxException e) {
            plugin.getLogger().log(Level.WARNING, "Invalid URL provided for " + type.name() + ": " + newUrl, e);
        }
    }

    /**
     * Removes a specific link by type
     */
    public void removeLink(ServerLinks.Type type) {
        ServerLinks serverLinks = Bukkit.getServer().getServerLinks();
        ServerLinks.ServerLink link = serverLinks.getLink(type);

        if (link != null) {
            boolean removed = serverLinks.removeLink(link);
            if (removed) {
                plugin.getLogger().info("Removed " + type.name() + " link");
            }
        } else {
            plugin.getLogger().warning("No link found for type: " + type.name());
        }
    }

    /**
     * Removes a custom link by searching for it
     */
    public void removeCustomLink(String url) {
        try {
            ServerLinks serverLinks = Bukkit.getServer().getServerLinks();
            URI targetUri = new URI(url);

            // Find and remove the link with matching URL
            serverLinks.getLinks().stream()
                    .filter(link -> link.getUrl().equals(targetUri))
                    .findFirst()
                    .ifPresentOrElse(
                            link -> {
                                boolean removed = serverLinks.removeLink(link);
                                if (removed) {
                                    plugin.getLogger().info("Removed custom link: " + url);
                                }
                            },
                            () -> plugin.getLogger().warning("No link found with URL: " + url)
                    );

        } catch (URISyntaxException e) {
            plugin.getLogger().log(Level.WARNING, "Invalid URL provided: " + url, e);
        }
    }

    /**
     * Lists all currently configured server links
     */
    public void listAllLinks() {
        ServerLinks serverLinks = Bukkit.getServer().getServerLinks();

        plugin.getLogger().info("Current server links:");
        serverLinks.getLinks().forEach(link -> {
            String displayName = link.getDisplayName();
            plugin.getLogger().info("- " + displayName + ": " + link.getUrl());
        });
    }

    /**
     * Gets a copy of the current server links configuration
     * Useful for backup or configuration management
     */
    public ServerLinks getServerLinksCopy() {
        return Bukkit.getServer().getServerLinks().copy();
    }

    /**
     * Checks if a specific link type exists
     */
    public boolean hasLinkType(ServerLinks.Type type) {
        ServerLinks serverLinks = Bukkit.getServer().getServerLinks();
        return serverLinks.getLink(type) != null;
    }

    /**
     * Gets the URL for a specific link type
     */
    public String getLinkUrl(ServerLinks.Type type) {
        ServerLinks serverLinks = Bukkit.getServer().getServerLinks();
        ServerLinks.ServerLink link = serverLinks.getLink(type);
        return link != null ? link.getUrl().toString() : null;
    }

}