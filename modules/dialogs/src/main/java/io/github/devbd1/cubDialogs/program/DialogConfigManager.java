package io.github.devbd1.cubDialogs.program;

import io.github.devbd1.cubDialogs.utilities.DialogConfigValidator;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads individual dialog files from /dialogs/ folder and builds Dialogs dynamically from the configuration.
 */
public class DialogConfigManager {
    private static JavaPlugin plugin;
    private static final Map<String, FileConfiguration> dialogConfigs = new ConcurrentHashMap<>();
    private static final Set<String> availableDialogIds = ConcurrentHashMap.newKeySet();

    private DialogConfigManager() {}

    public static void init(JavaPlugin pl) {
        plugin = pl;
        ensureDialogsFolder();
        loadAllDialogConfigs();
    }

    public static NamedTextColor parseNamedColor(String v) {
        NamedTextColor color = NamedTextColor.NAMES.value(v);
        if (color == null) {
            throw new IllegalArgumentException("Unknown named color: " + v);
        }
        return color;
    }

    private static void ensureDialogsFolder() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dataFolder.mkdirs();
        }

        File dialogsFolder = new File(dataFolder, "dialogs");
        if (!dialogsFolder.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dialogsFolder.mkdirs();
            plugin.getLogger().info("Created dialogs folder at: " + dialogsFolder.getPath());
        }

        // Copy default dialog files from resources if they exist
        copyDefaultDialogFiles(dialogsFolder);
    }

    private static void copyDefaultDialogFiles(File dialogsFolder) {
        // List of default dialog files to copy from resources
        String[] defaultDialogs = {"dialog_configuration_template.yml", "player_settings.yml", "feedback_form.yml", "exp_config.yml"};

        for (String dialogFile : defaultDialogs) {
            File targetFile = new File(dialogsFolder, dialogFile);
            if (!targetFile.exists()) {
                try {
                    plugin.saveResource("dialogs/" + dialogFile, false);
                    plugin.getLogger().info("Created default dialog file: " + dialogFile);
                } catch (IllegalArgumentException ignored) {
                    // Resource doesn't exist in jar, skip
                    plugin.getLogger().fine("Default dialog resource not found: dialogs/" + dialogFile);
                }
            }
        }
    }

    private static void loadAllDialogConfigs() {
        File dialogsFolder = new File(plugin.getDataFolder(), "dialogs");
        if (!dialogsFolder.exists() || !dialogsFolder.isDirectory()) {
            plugin.getLogger().warning("Dialogs folder not found or is not a directory!");
            return;
        }

        File[] yamlFiles = dialogsFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
        if (yamlFiles == null || yamlFiles.length == 0) {
            plugin.getLogger().warning("No dialog configuration files found in dialogs folder!");
            return;
        }

        dialogConfigs.clear();
        availableDialogIds.clear();

        for (File yamlFile : yamlFiles) {
            String dialogId = yamlFile.getName().substring(0, yamlFile.getName().length() - 4); // Remove .yml extension

            try {
                FileConfiguration config = YamlConfiguration.loadConfiguration(yamlFile);
                dialogConfigs.put(dialogId, config);
                availableDialogIds.add(dialogId);
                plugin.getLogger().info("Loaded dialog configuration: " + dialogId);
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to load dialog configuration from " + yamlFile.getName() + ": " + e.getMessage());
            }
        }

        plugin.getLogger().info("Loaded " + dialogConfigs.size() + " dialog configurations");

        // Validate all loaded configurations
        List<DialogConfigValidator.ValidationIssue> issues = DialogConfigValidator.validateAllDialogs(plugin);
        DialogConfigValidator.logValidationIssues(plugin, issues);
    }

    public static Dialog buildDialog(String id) {
        if (plugin == null) {
            // Lazy safety if init wasn't called
            init(JavaPlugin.getProvidingPlugin(DialogConfigManager.class));
        }

        plugin.getLogger().info("[DEBUG] Building dialog with ID: " + id);

        FileConfiguration dialogConfig = dialogConfigs.get(id);
        if (dialogConfig == null) {
            plugin.getLogger().warning("Dialog configuration not found for ID: " + id);
            plugin.getLogger().info("Available dialog IDs: " + String.join(", ", availableDialogIds));
            return null;
        }

        plugin.getLogger().info("[DEBUG] Found dialog config for: " + id);

        // The root of the dialog config is the entire file, not a subsection
        ConfigurationSection root = dialogConfig;

        // Title
        Component title = readComponent(root.getConfigurationSection("title"),
                Component.text("Dialog"));
        plugin.getLogger().info("[DEBUG] Dialog title: " + title.toString());

        // Inputs - Fixed approach
        List<DialogInput> inputs = new ArrayList<>();

        // Get inputs as raw list
        List<?> inputsList = root.getList("inputs");
        plugin.getLogger().info("[DEBUG] Inputs as list: " + (inputsList != null ? inputsList.size() + " items" : "null"));

        if (inputsList != null && !inputsList.isEmpty()) {
            plugin.getLogger().info("[DEBUG] Processing inputs as list...");
            for (int i = 0; i < inputsList.size(); i++) {
                Object inputData = inputsList.get(i);
                plugin.getLogger().info("[DEBUG] Processing input " + i + " of type: " + inputData.getClass().getName());

                if (inputData instanceof java.util.Map<?, ?> inputMap) {
                    plugin.getLogger().info("[DEBUG] Input " + i + " map keys: " + inputMap.keySet());

                    // Convert the map to a temporary configuration section for easier processing
                    org.bukkit.configuration.MemoryConfiguration tempConfig = new org.bukkit.configuration.MemoryConfiguration();
                    for (Map.Entry<?, ?> entry : inputMap.entrySet()) {
                        tempConfig.set(entry.getKey().toString(), entry.getValue());
                    }

                    DialogInput built = buildInputFromMap(tempConfig);
                    if (built != null) {
                        inputs.add(built);
                        plugin.getLogger().info("[DEBUG] Successfully built input " + i);
                    } else {
                        plugin.getLogger().warning("[DEBUG] Failed to build input " + i);
                    }
                } else {
                    plugin.getLogger().warning("[DEBUG] Input " + i + " is not a map, skipping");
                }
            }
        } else {
            plugin.getLogger().warning("[DEBUG] No inputs found in dialog config!");
        }

        plugin.getLogger().info("[DEBUG] Total inputs built: " + inputs.size());

        DialogBase base = DialogBase.builder(title)
                .inputs(inputs)
                .build();
        plugin.getLogger().info("[DEBUG] DialogBase created with " + inputs.size() + " inputs");

        String type = root.getString("type", "confirmation").toLowerCase(Locale.ROOT);
        DialogType dialogType = switch (type) {
            case "confirmation" -> buildConfirmationType(root.getConfigurationSection("buttons"));
            default -> {
                plugin.getLogger().warning("Unsupported dialog type: " + type + " (using confirmation)");
                yield buildConfirmationType(root.getConfigurationSection("buttons"));
            }
        };

        Dialog dialog = Dialog.create(b -> b.empty().base(base).type(dialogType));
        plugin.getLogger().info("[DEBUG] Final dialog created successfully");

        return dialog;
    }

    // New method to build input from a map-based configuration
    private static DialogInput buildInputFromMap(org.bukkit.configuration.Configuration config) {
        plugin.getLogger().info("[DEBUG] Building input from map-based config");

        String id = config.getString("id");
        plugin.getLogger().info("[DEBUG] Input ID: " + id);

        if (id == null || id.isBlank()) {
            plugin.getLogger().warning("Dialog input is missing 'id' field");
            return null;
        }

        String type = config.getString("type", "text").toLowerCase(Locale.ROOT);
        plugin.getLogger().info("[DEBUG] Input type: " + type);

        // Parse label - FIXED VERSION
        Component label;
        Object labelObj = config.get("label");
        if (labelObj instanceof java.util.Map<?, ?> labelMap) {
            // Complex label object (with text/color properties)
            org.bukkit.configuration.MemoryConfiguration labelConfig = new org.bukkit.configuration.MemoryConfiguration();
            for (Map.Entry<?, ?> entry : labelMap.entrySet()) {
                labelConfig.set(entry.getKey().toString(), entry.getValue());
            }
            label = readComponent(labelConfig, Component.text(id));
        } else if (labelObj instanceof String labelString) {
            // Simple string label - parse with MiniMessage or legacy formatting
            label = parseFormattedText(labelString);
        } else {
            // Fallback to ID if no label found
            label = Component.text(id);
        }

        plugin.getLogger().info("[DEBUG] Input label: " + label.toString());

        DialogInput result = switch (type) {
            case "number_range" -> {
                plugin.getLogger().info("[DEBUG] Building number_range input");
                float min = (float) config.getDouble("min", 0);
                float max = (float) config.getDouble("max", 100);
                float step = (float) config.getDouble("step", 1);
                float initial = (float) config.getDouble("initial", min);
                int width = config.getInt("width", 300);
                String labelFormat = config.getString("label_format", null);

                plugin.getLogger().info("[DEBUG] Number range - min:" + min + ", max:" + max + ", step:" + step + ", initial:" + initial + ", width:" + width);

                var builder = DialogInput.numberRange(id, label, min, max)
                        .step(step)
                        .initial(initial)
                        .width(width);

                if (labelFormat != null && !labelFormat.isBlank()) {
                    builder = builder.labelFormat(labelFormat);
                    plugin.getLogger().info("[DEBUG] Applied label format: " + labelFormat);
                }

                yield builder.build();
            }
            case "text" -> {
                plugin.getLogger().info("[DEBUG] Building text input");
                String initial = config.getString("initial", "");
                int width = config.getInt("width", 300);
                int maxLength = config.getInt("max_length", 0);
                boolean labelVisible = config.getBoolean("label_visible", true);

                plugin.getLogger().info("[DEBUG] Text - initial:'" + initial + "', width:" + width + ", maxLength:" + maxLength + ", labelVisible:" + labelVisible);

                var builder = DialogInput.text(id, label)
                        .initial(initial)
                        .width(width)
                        .labelVisible(labelVisible);

                if (maxLength > 0) {
                    builder = builder.maxLength(maxLength);
                    plugin.getLogger().info("[DEBUG] Applied max length: " + maxLength);
                }

                // Handle multiline
                Object multilineObj = config.get("multiline");
                if (multilineObj instanceof java.util.Map<?, ?> multiMap) {
                    org.bukkit.configuration.MemoryConfiguration multiConfig = new org.bukkit.configuration.MemoryConfiguration();
                    for (Map.Entry<?, ?> entry : multiMap.entrySet()) {
                        multiConfig.set(entry.getKey().toString(), entry.getValue());
                    }
                    int maxLines = multiConfig.getInt("max_lines", 1);
                    int maxColumns = multiConfig.getInt("max_columns", 50);
                    builder = builder.multiline(TextDialogInput.MultilineOptions.create(maxLines, maxColumns));
                    plugin.getLogger().info("[DEBUG] Applied multiline - maxLines:" + maxLines + ", maxColumns:" + maxColumns);
                }

                yield builder.build();
            }
            default -> {
                plugin.getLogger().warning("Unsupported input type: " + type + " for id=" + id);
                yield null;
            }
        };

        plugin.getLogger().info("[DEBUG] Built input result: " + (result != null ? "SUCCESS" : "FAILED"));
        return result;
    }

    /**
     * Parses formatted text (MiniMessage format like <aqua>text</aqua> or legacy &a format)
     */
    private static Component parseFormattedText(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }

        // Use MiniMessage to parse the text
        try {
            return net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(text);
        } catch (Exception e) {
            // If MiniMessage parsing fails, try legacy format
            try {
                return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(text);
            } catch (Exception e2) {
                // If both fail, return plain text
                return Component.text(text);
            }
        }
    }


    // Update readComponent to handle both ConfigurationSection and Configuration
    private static Component readComponent(org.bukkit.configuration.Configuration config, Component def) {
        if (config == null) return def;
        String text = config.getString("text", "");
        TextColor color = namedOrHex(config.getString("color", null));
        return color != null ? Component.text(text, color) : Component.text(text);
    }

    private static DialogType buildConfirmationType(ConfigurationSection buttonsSec) {
        if (buttonsSec == null) {
            // Fallback buttons
            ActionButton confirm = ActionButton.create(
                    Component.text("Confirm", namedOrHex("green")),
                    Component.text("Click to confirm."),
                    100,
                    null
            );
            ActionButton cancel = ActionButton.create(
                    Component.text("Cancel", namedOrHex("red")),
                    Component.text("Click to cancel."),
                    100,
                    null
            );
            return DialogType.confirmation(confirm, cancel);
        }

        ActionButton confirm = buildButton(buttonsSec.getConfigurationSection("confirm"),
                "Confirm", "Click to confirm.", "green", 100);
        ActionButton cancel = buildButton(buttonsSec.getConfigurationSection("cancel"),
                "Cancel", "Click to cancel.", "red", 100);

        return DialogType.confirmation(confirm, cancel);
    }

    private static ActionButton buildButton(ConfigurationSection sec,
                                            String defText,
                                            String defDesc,
                                            String defColor,
                                            int defWeight) {
        if (sec == null) {
            return ActionButton.create(
                    Component.text(defText, namedOrHex(defColor)),
                    Component.text(defDesc),
                    defWeight,
                    null
            );
        }

        String text = sec.getString("text", defText);
        String desc = sec.getString("description", defDesc);
        String colorStr = sec.getString("color", defColor);
        int weight = sec.getInt("weight", defWeight);
        DialogAction action = buildAction(sec.getConfigurationSection("action"));

        return ActionButton.create(
                Component.text(text, namedOrHex(colorStr)),
                Component.text(desc),
                weight,
                action
        );
    }

    private static DialogAction buildAction(ConfigurationSection sec) {
        if (sec == null) return null;
        String type = sec.getString("type", "none").toLowerCase(Locale.ROOT);
        return switch (type) {
            case "custom_click" -> {
                String key = sec.getString("key", "papermc:user_input/confirm");
                yield DialogAction.customClick(Key.key(key), null);
            }
            case "none", "null" -> null;
            default -> {
                plugin.getLogger().warning("Unsupported action type: " + type);
                yield null;
            }
        };
    }

    private static DialogInput buildInput(ConfigurationSection in) {
        plugin.getLogger().info("[DEBUG] Building input from config section");
        plugin.getLogger().info("[DEBUG] Input config keys: " + in.getKeys(true));
        plugin.getLogger().info("[DEBUG] Full input config: " + in.getValues(true));

        String id = in.getString("id");
        plugin.getLogger().info("[DEBUG] Input ID: " + id);

        if (id == null || id.isBlank()) {
            plugin.getLogger().warning("Dialog input is missing 'id' field");
            return null;
        }

        String type = in.getString("type", "text").toLowerCase(Locale.ROOT);
        plugin.getLogger().info("[DEBUG] Input type: " + type);

        Component label = readComponent(in.getConfigurationSection("label"),
                Component.text(id));
        plugin.getLogger().info("[DEBUG] Input label: " + label.toString());

        DialogInput result = switch (type) {
            case "number_range" -> {
                plugin.getLogger().info("[DEBUG] Building number_range input");
                float min = (float) in.getDouble("min", 0);
                float max = (float) in.getDouble("max", 100);
                float step = (float) in.getDouble("step", 1);
                float initial = (float) in.getDouble("initial", min);
                int width = in.getInt("width", 300);
                String labelFormat = in.getString("label_format", null);

                plugin.getLogger().info("[DEBUG] Number range - min:" + min + ", max:" + max + ", step:" + step + ", initial:" + initial + ", width:" + width);

                var builder = DialogInput.numberRange(id, label, min, max)
                        .step(step)
                        .initial(initial)
                        .width(width);

                if (labelFormat != null && !labelFormat.isBlank()) {
                    builder = builder.labelFormat(labelFormat);
                    plugin.getLogger().info("[DEBUG] Applied label format: " + labelFormat);
                }

                yield builder.build();
            }
            case "text" -> {
                plugin.getLogger().info("[DEBUG] Building text input");
                String initial = in.getString("initial", "");
                int width = in.getInt("width", 300);
                int maxLength = in.getInt("max_length", 0);
                boolean labelVisible = in.getBoolean("label_visible", true);

                plugin.getLogger().info("[DEBUG] Text - initial:'" + initial + "', width:" + width + ", maxLength:" + maxLength + ", labelVisible:" + labelVisible);

                var builder = DialogInput.text(id, label)
                        .initial(initial)
                        .width(width)
                        .labelVisible(labelVisible);

                if (maxLength > 0) {
                    builder = builder.maxLength(maxLength);
                    plugin.getLogger().info("[DEBUG] Applied max length: " + maxLength);
                }

                ConfigurationSection multi = in.getConfigurationSection("multiline");
                if (multi != null) {
                    int maxLines = multi.getInt("max_lines", 1);
                    int maxColumns = multi.getInt("max_columns", 50);
                    builder = builder.multiline(TextDialogInput.MultilineOptions.create(maxLines, maxColumns));
                    plugin.getLogger().info("[DEBUG] Applied multiline - maxLines:" + maxLines + ", maxColumns:" + maxColumns);
                }

                yield builder.build();
            }
            default -> {
                plugin.getLogger().warning("Unsupported input type: " + type + " for id=" + id);
                yield null;
            }
        };

        plugin.getLogger().info("[DEBUG] Built input result: " + (result != null ? "SUCCESS" : "FAILED"));
        return result;
    }

    private static Component readComponent(ConfigurationSection sec, Component def) {
        if (sec == null) return def;
        String text = sec.getString("text", "");
        TextColor color = namedOrHex(sec.getString("color", null));
        return color != null ? Component.text(text, color) : Component.text(text);
    }

    private static TextColor namedOrHex(String value) {
        if (value == null || value.isBlank()) return null;

        String v = value.trim();

        // Try hex with leading '#'
        if (v.startsWith("#")) {
            TextColor hex = TextColor.fromHexString(v);
            if (hex != null) return hex;
        }

        // Try named color
        NamedTextColor named = NamedTextColor.NAMES.value(v); // case-insensitive lookup
        if (named != null) return named;

        // Best-effort: try hex without '#'
        TextColor hex = TextColor.fromHexString("#" + v);
        return hex; // may be null if invalid
    }

    /**
     * Gets all available dialog IDs.
     * @return List of dialog IDs (file names without .yml extension)
     */
    public static java.util.List<String> getDialogIds() {
        if (plugin == null) {
            throw new IllegalStateException("DialogConfigManager is not initialized. Call DialogConfigManager.init(plugin) first.");
        }

        return new java.util.ArrayList<>(availableDialogIds);
    }

    /**
     * Reloads all dialog configurations from the dialogs folder.
     * Useful for runtime configuration updates.
     */
    public static void reloadDialogConfigs() {
        if (plugin == null) {
            throw new IllegalStateException("DialogConfigManager is not initialized. Call DialogConfigManager.init(plugin) first.");
        }

        loadAllDialogConfigs();
        plugin.getLogger().info("Reloaded all dialog configurations");
    }

    /**
     * Checks if a dialog with the given ID exists.
     * @param dialogId The dialog ID to check
     * @return true if the dialog exists, false otherwise
     */
    public static boolean hasDialog(String dialogId) {
        return availableDialogIds.contains(dialogId);
    }
}