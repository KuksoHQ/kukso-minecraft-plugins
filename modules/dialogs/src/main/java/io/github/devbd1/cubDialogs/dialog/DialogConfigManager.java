package io.github.devbd1.cubDialogs.dialog;

import io.github.devbd1.cubDialogs.dialog.components.BodyBuilder;
import io.github.devbd1.cubDialogs.dialog.components.ButtonBuilder;
import io.github.devbd1.cubDialogs.dialog.components.InputBuilder;

import io.github.devbd1.cubDialogs.dialog.types.TypeInterface;
import io.github.devbd1.cubDialogs.dialog.types.TypeRegistrar;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
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

    private static BodyBuilder bodyBuilder;
    private static ButtonBuilder buttonBuilder;
    private static InputBuilder inputBuilder;

    private DialogConfigManager() {}

    public static void init(JavaPlugin pl) {
        plugin = pl;
        bodyBuilder = new BodyBuilder(pl);
        buttonBuilder = new ButtonBuilder(pl);
        inputBuilder = new InputBuilder(pl);

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

        copyDefaultDialogFiles(dialogsFolder);
    }

    private static void copyDefaultDialogFiles(File dialogsFolder) {
        // Non-template example configs that should live directly under /dialogs
        String[] baseDialogs = {
                "exp_config.yml",
                "feedback_form.yml",
                "server_rules.yml"
        };

        // Template files that should live under /dialogs/templates
        String[] templateDialogs = {
                "confirmation.yml",
                "multiaction.yml",
                "notice.yml",
                "reward_notice.yml"
        };

        // Copy base dialogs to dialogs/
        for (String dialogFile : baseDialogs) {
            File targetFile = new File(dialogsFolder, dialogFile);
            if (!targetFile.exists()) {
                try {
                    plugin.saveResource("dialogs/" + dialogFile, false);
                    plugin.getLogger().info("Created default dialog file: " + dialogFile);
                } catch (IllegalArgumentException ignored) {
                    plugin.getLogger().fine("Default dialog resource not found: dialogs/" + dialogFile);
                }
            }
        }

        // Copy templates to dialogs/templates/
        File templatesFolder = new File(dialogsFolder, "templates");
        for (String dialogFile : templateDialogs) {
            File targetFile = new File(templatesFolder, dialogFile);
            if (!targetFile.exists()) {
                try {
                    plugin.saveResource("dialogs/templates/" + dialogFile, false);
                    plugin.getLogger().info("Created default template dialog file: " + dialogFile);
                } catch (IllegalArgumentException ignored) {
                    plugin.getLogger().fine("Default dialog resource not found: dialogs/templates/" + dialogFile);
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

        List<File> yamlFiles = collectYamlFilesRecursive(dialogsFolder);
        if (yamlFiles.isEmpty()) {
            plugin.getLogger().warning("No dialog configuration files found in dialogs folder!");
            return;
        }

        dialogConfigs.clear();
        availableDialogIds.clear();

        for (File yamlFile : yamlFiles) {
            String dialogId = toRelativeId(dialogsFolder, yamlFile);

            try {
                FileConfiguration config = YamlConfiguration.loadConfiguration(yamlFile);
                dialogConfigs.put(dialogId, config);
                availableDialogIds.add(dialogId);
                plugin.getLogger().info("Loaded dialog configuration: " + dialogId);
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to load dialog configuration from " + yamlFile.getPath() + ": " + e.getMessage());
            }
        }

        plugin.getLogger().info("Loaded " + dialogConfigs.size() + " dialog configurations");

        // Validate all loaded configurations
        List<DialogConfigValidator.ValidationIssue> issues = DialogConfigValidator.validateAllDialogs(plugin);
        DialogConfigValidator.logValidationIssues(plugin, issues);
    }

    private static List<File> collectYamlFilesRecursive(File root) {
        List<File> files = new ArrayList<>();
        File[] children = root.listFiles();
        if (children == null) return files;

        for (File child : children) {
            if (child.isDirectory()) {
                files.addAll(collectYamlFilesRecursive(child));
            } else if (child.isFile() && child.getName().toLowerCase(Locale.ROOT).endsWith(".yml")) {
                files.add(child);
            }
        }
        return files;
    }

    private static String toRelativeId(File root, File file) {
        String rel = root.toPath().relativize(file.toPath()).toString();
        rel = rel.replace(File.separatorChar, '/');
        if (rel.toLowerCase(Locale.ROOT).endsWith(".yml")) {
            rel = rel.substring(0, rel.length() - 4);
        }
        return rel;
    }

    /**
     * Returns true if the dialog is enabled in its config. Defaults to true when missing.
     */
    public static boolean isEnabled(String id) {
        FileConfiguration cfg = dialogConfigs.get(id);
        if (cfg == null) return false;
        return cfg.getBoolean("enabled", true);
    }

    /**
     * Reads the permission required for a viewer to open this dialog for themselves.
     * Falls back to a sensible default if missing.
     */
    public static String getPermissionToOpen(String id) {
        FileConfiguration cfg = dialogConfigs.get(id);
        if (cfg == null) return "cubDialogs.dialog.open.*";
        return cfg.getString("permission_to_open", "cubDialogs.dialog.open.*");
    }

    /**
     * Reads the permission required to open this dialog for another player (remote).
     * Falls back to a sensible default if missing.
     */
    public static String getPermissionToOpenRemote(String id) {
        FileConfiguration cfg = dialogConfigs.get(id);
        if (cfg == null) return "cubDialogs.dialog.remote.*";
        return cfg.getString("permission_to_open_remote", "cubDialogs.dialog.remote.*");
    }

    public static Dialog buildDialog(String id) {
        if (plugin == null) {
            // Lazy safety if init wasn't called
            init(JavaPlugin.getProvidingPlugin(DialogConfigManager.class));
        }

        //plugin.getLogger().info("[DEBUG] Building dialog with ID: " + id);

        FileConfiguration dialogConfig = dialogConfigs.get(id);
        if (dialogConfig == null) {
            plugin.getLogger().warning("Dialog configuration not found for ID: " + id);
            plugin.getLogger().info("Available dialog IDs: " + String.join(", ", availableDialogIds));
            return null;
        }

        //plugin.getLogger().info("[DEBUG] Found dialog config for: " + id);

        // The root of the dialog config is the entire file, not a subsection
        ConfigurationSection root = dialogConfig;

        // Title
        String titleText = root.getString("title", "Default Title");
        Component title = parseFormattedText(titleText);
        //plugin.getLogger().info("[DEBUG] Dialog title: " + title.toString());

        // External Title
        String externalTitleText = root.getString("external_title", "Default Title");
        Component externalTitle = parseFormattedText(externalTitleText);
        //plugin.getLogger().info("[DEBUG] Dialog externalTitle: " + externalTitle.toString());

        boolean canCloseWithEscape = root.getBoolean("can_close_with_escape", true);


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

        // In buildDialog() method, replace the entire body processing section with this simplified version:

        // Dialog Bodies (using bodies: configuration only)
        List<DialogBody> dialogBodies = new ArrayList<>();

        List<?> bodiesList = root.getList("bodies");
        if (bodiesList != null && !bodiesList.isEmpty()) {
            plugin.getLogger().info("[DEBUG] Processing " + bodiesList.size() + " dialog bodies...");

            for (int i = 0; i < bodiesList.size(); i++) {
                Object bodyData = bodiesList.get(i);
                plugin.getLogger().info("[DEBUG] Processing body " + i + " of type: " + bodyData.getClass().getName());

                DialogBody body = processBodyData(bodyData);
                if (body != null) {
                    dialogBodies.add(body);
                    plugin.getLogger().info("[DEBUG] Successfully built dialog body " + i);
                } else {
                    plugin.getLogger().warning("[DEBUG] Failed to build dialog body " + i);
                }
            }
        } else {
            plugin.getLogger().info("[DEBUG] No bodies configuration found");
        }

        plugin.getLogger().info("[DEBUG] Total dialog bodies built: " + dialogBodies.size());

        // Update the DialogBase creation
        DialogBase base;
        if (!dialogBodies.isEmpty()) {
            base = DialogBase.builder(title)
                    .externalTitle(externalTitle)
                    .canCloseWithEscape(canCloseWithEscape)
                    .body(dialogBodies)
                    .inputs(inputs)
                    .build();
            plugin.getLogger().info("[DEBUG] DialogBase created with " + dialogBodies.size() + " bodies and " + inputs.size() + " inputs");
        } else {
            base = DialogBase.builder(title)
                    .externalTitle(externalTitle)
                    .canCloseWithEscape(canCloseWithEscape)
                    .inputs(inputs)
                    .build();
            plugin.getLogger().info("[DEBUG] DialogBase created with " + inputs.size() + " inputs (no bodies)");
        }

        String type = root.getString("type", "notice").toLowerCase(Locale.ROOT);
        TypeInterface handler = TypeRegistrar.getHandler(type);

        DialogType dialogType;
        if (handler != null) {
            dialogType = handler.buildDialogType(root);
        } else {
            plugin.getLogger().warning("Unsupported dialog type: " + type + " (using confirmation)");
            dialogType = TypeRegistrar.getHandler("confirmation").buildDialogType(root);
        }

        Dialog dialog = Dialog.create(b -> b.empty().base(base).type(dialogType));
        plugin.getLogger().info("[DEBUG] Final dialog created successfully");

        return dialog;
    }

    /**
     * Parses formatted text (ColorManager + MiniMessage format)
     */
    public static Component parseFormattedText(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }

        // First apply ColorManager formatting (handles &, hex, gradients)
        String processedText = io.github.devbd1.cubDialogs.utilities.ColorManager.applyColorFormatting(text);

        // Use MiniMessage to parse the processed text
        try {
            return net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(processedText);
        } catch (Exception e) {
            // If MiniMessage parsing fails, try legacy format
            try {
                return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(processedText);
            } catch (Exception e2) {
                // If both fail, return plain text
                return Component.text(processedText);
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

    private static Component readComponent(ConfigurationSection sec, Component def) {
        if (sec == null) return def;
        String text = sec.getString("text", "");
        TextColor color = namedOrHex(sec.getString("color", null));
        return color != null ? Component.text(text, color) : Component.text(text);
    }

    public static TextColor namedOrHex(String value) {
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

    private static DialogBody processBodyData(Object bodyData) {
        if (bodyData instanceof java.util.Map<?, ?> bodyMap) {
            org.bukkit.configuration.MemoryConfiguration tempConfig = new org.bukkit.configuration.MemoryConfiguration();
            for (Map.Entry<?, ?> entry : bodyMap.entrySet()) {
                tempConfig.set(entry.getKey().toString(), entry.getValue());
            }
            return bodyBuilder.buildBody(tempConfig);
        } else if (bodyData instanceof ConfigurationSection bodySection) {
            if (bodySection instanceof org.bukkit.configuration.Configuration config) {
                return bodyBuilder.buildBody(config);
            } else {
                org.bukkit.configuration.MemoryConfiguration tempConfig = new org.bukkit.configuration.MemoryConfiguration();
                for (String key : bodySection.getKeys(true)) {
                    tempConfig.set(key, bodySection.get(key));
                }
                return bodyBuilder.buildBody(tempConfig);
            }
        }
        return null;
    }

    public static ActionButton buildButton(ConfigurationSection sec, String defText, String defDesc, String defColor, int defWidth) {
        return buttonBuilder.buildButton(sec, defText, defDesc, defColor, defWidth);
    }

    private static DialogInput buildInputFromMap(org.bukkit.configuration.Configuration config) {
        plugin.getLogger().info("[DEBUG] Building input from map-based config");

        // Parse label
        Component label;
        Object labelObj = config.get("label");
        if (labelObj instanceof java.util.Map<?, ?> labelMap) {
            // Complex label object (with text/color properties)
            org.bukkit.configuration.MemoryConfiguration labelConfig = new org.bukkit.configuration.MemoryConfiguration();
            for (Map.Entry<?, ?> entry : labelMap.entrySet()) {
                labelConfig.set(entry.getKey().toString(), entry.getValue());
            }
            label = readComponent(labelConfig, Component.text(config.getString("id", "")));
        } else if (labelObj instanceof String labelString) {
            // Simple string label - parse with MiniMessage or legacy formatting
            label = parseFormattedText(labelString);
        } else {
            // Fallback to ID if no label found
            label = Component.text(config.getString("id", ""));
        }

        plugin.getLogger().info("[DEBUG] Input label: " + label.toString());

        // Use the new builder to create the input
        DialogInput result = inputBuilder.buildInput(config, label);

        plugin.getLogger().info("[DEBUG] Built input result: " + (result != null ? "SUCCESS" : "FAILED"));
        return result;
    }
}