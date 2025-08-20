package io.github.devbd1.cubDialogs.dialog.builders;

import io.github.devbd1.cubDialogs.dialog.DialogConfigManager;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Handles the creation of dialog bodies based on configuration data.
 */
public class BodyBuilder {
    private final JavaPlugin plugin;

    public BodyBuilder(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Builds a DialogBody from a map-based configuration.
     *
     * @param config The configuration containing body settings
     * @return A DialogBody instance or null if building failed
     */
    public DialogBody buildBody(Configuration config) {
        if (config == null) {
            return null;
        }

        //plugin.getLogger().info("[DEBUG] Building dialog body from config");

        String type = config.getString("type");
        if (type == null) {
            plugin.getLogger().warning("Dialog body is missing required 'type' field");
            return null;
        }

        type = type.toLowerCase(Locale.ROOT);
        //plugin.getLogger().info("[DEBUG] Dialog body type: " + type);

        return switch (type) {
            case "plain_message" -> buildPlainMessageBody(config);
            case "item" -> buildItemBody(config);
            default -> {
                plugin.getLogger().warning("Unsupported dialog body type: " + type);
                yield null;
            }
        };
    }

    private DialogBody buildPlainMessageBody(Configuration config) {
        //plugin.getLogger().info("[DEBUG] Building plain_message dialog body");

        // Get message content
        String message = config.getString("message", config.getString("text", ""));
        int width = config.getInt("width", 300);

        // Parse the message text with formatting support
        Component messageComponent = DialogConfigManager.parseFormattedText(message);

        //plugin.getLogger().info("[DEBUG] Plain message body - message:'" + message + "', width:" + width);

        return DialogBody.plainMessage(messageComponent, width);
    }

    private DialogBody buildItemBody(Configuration config) {
        //plugin.getLogger().info("[DEBUG] Building item dialog body");

        // Get item configuration
        String materialName = config.getString("material", "STONE");
        int amount = config.getInt("amount", 1);
        String displayName = config.getString("display_name", null);
        Object loreObj = config.get("lore");

        // Get item dialog body specific settings
        boolean showTooltip = config.getBoolean("show_tooltip", true);
        boolean showStackCount = config.getBoolean("show_stack_count", true);
        int width = config.getInt("width", 300);
        int height = config.getInt("height", 300);
        String desc = config.getString("description", null);

        //plugin.getLogger().info("[DEBUG] Item body - material:" + materialName + ", amount:" + amount + ", showTooltip:" + showTooltip + ", showStackCount:" + showStackCount);

        try {
            // Create ItemStack
            org.bukkit.Material material = org.bukkit.Material.valueOf(materialName.toUpperCase());
            ItemStack itemStack = new ItemStack(material, amount);

            // Apply display name and lore if provided
            org.bukkit.inventory.meta.ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                if (displayName != null && !displayName.isBlank()) {
                    // Apply ColorManager formatting and convert to legacy format for ItemMeta
                    String formattedDisplayName = io.github.devbd1.cubDialogs.utilities.ColorManager.applyColorFormatting(displayName);
                    meta.setDisplayName(formattedDisplayName);
                }

                if (loreObj instanceof List<?> loreList) {
                    List<String> lore = new ArrayList<>();
                    for (Object loreItem : loreList) {
                        if (loreItem instanceof String loreString) {
                            // Apply ColorManager formatting to each lore line
                            String formattedLore = io.github.devbd1.cubDialogs.utilities.ColorManager.applyColorFormatting(loreString);
                            lore.add(formattedLore);
                        }
                    }
                    meta.setLore(lore);
                }

                itemStack.setItemMeta(meta);
            }

            // Build the item dialog body
            var builder = DialogBody.item(itemStack);
            
            if (width > 0) {
                builder = builder.width(width);
            }
            
            if (height > 0) {
                builder = builder.height(height);
            }
            
            builder = builder.showTooltip(showTooltip)
                    .showDecorations(showStackCount);

            if (desc != null && !desc.isBlank()) {
                builder = builder.description(DialogBody.plainMessage(DialogConfigManager.parseFormattedText(desc), 100));
            }


            return builder.build();

        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("[DEBUG] Invalid material: " + materialName + " for dialog body");
            return null;
        } catch (Exception e) {
            plugin.getLogger().severe("[DEBUG] Error creating item dialog body: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
