package io.github.devbd1.cubDialogs.dialog.builders;

import io.github.devbd1.cubDialogs.dialog.DialogConfigManager;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Locale;

/**
 * Handles the creation of dialog inputs based on configuration data.
 */
public class InputBuilder {
    private final JavaPlugin plugin;

    public InputBuilder(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Builds a DialogInput from a map-based configuration.
     *
     * @param config The configuration containing input settings
     * @param label The component label for the input
     * @return A DialogInput instance or null if building failed
     */
    public DialogInput buildInput(Configuration config, Component label) {
        String id = config.getString("id");
        //plugin.getLogger().info("[DEBUG] Building input with ID: " + id);

        if (id == null || id.isBlank()) {
            plugin.getLogger().warning("Dialog input is missing 'id' field");
            return null;
        }

        String type = config.getString("type", "text").toLowerCase(Locale.ROOT);
        //plugin.getLogger().info("[DEBUG] Input type: " + type);

        return switch (type) {
            case "boolean" -> buildBooleanInput(id, label, config);
            case "number_range" -> buildNumberRangeInput(id, label, config);
            case "single_option" -> buildSingleOptionInput(id, label, config);
            case "text" -> buildTextInput(id, label, config);
            default -> {
                plugin.getLogger().warning("Unsupported input type: " + type + " for id=" + id);
                yield null;
            }
        };
    }

    private DialogInput buildBooleanInput(String id, Component label, Configuration config) {
        //plugin.getLogger().info("[DEBUG] Building boolean input");
        boolean initial = config.getBoolean("initial", false);
        boolean labelVisible = config.getBoolean("label_visible", true);
        int width = config.getInt("width", 300);

        // Get true/false values if specified (default to true/false)
        Object onTrueObj = config.get("on_true");
        Object onFalseObj = config.get("on_false");

        //plugin.getLogger().info("[DEBUG] Boolean - initial:" + initial + ", labelVisible:" + labelVisible + ", width:" + width);

        // Build the boolean input with custom true/false values if provided
        var builder = DialogInput.bool(id, label)
                .initial(initial);

        // Add true/false values if specified
        if (onTrueObj != null) {
            if (onTrueObj instanceof String strValue) {
                builder = builder.onTrue(strValue);
            } else {
                plugin.getLogger().info("[DEBUG] Couldn't apply custom true value because it is not a string: " + onTrueObj);
            }
            //plugin.getLogger().info("[DEBUG] Applied custom true value: " + onTrueObj);
        }

        if (onFalseObj != null) {
            if (onFalseObj instanceof String strValue) {
                builder = builder.onFalse(strValue);
            } else {
                plugin.getLogger().info("[DEBUG] Couldn't apply custom false value because it is not a string: " + onTrueObj);
            }
            //plugin.getLogger().info("[DEBUG] Applied custom false value: " + onFalseObj);
        }

        return builder.build();
    }

    private DialogInput buildNumberRangeInput(String id, Component label, Configuration config) {
        //plugin.getLogger().info("[DEBUG] Building number_range input");
        float min = (float) config.getDouble("min", 0);
        float max = (float) config.getDouble("max", 100);
        float step = (float) config.getDouble("step", 1);
        float initial = (float) config.getDouble("initial", min);
        int width = config.getInt("width", 300);
        String labelFormat = config.getString("label_format", null);

        //plugin.getLogger().info("[DEBUG] Number range - min:" + min + ", max:" + max + ", step:" + step + ", initial:" + initial + ", width:" + width);

        var builder = DialogInput.numberRange(id, label, min, max)
                .step(step)
                .initial(initial)
                .width(width);

        if (labelFormat != null && !labelFormat.isBlank()) {
            builder = builder.labelFormat(labelFormat);
            //plugin.getLogger().info("[DEBUG] Applied label format: " + labelFormat);
        }

        return builder.build();
    }

    private DialogInput buildTextInput(String id, Component label, Configuration config) {
        //.getLogger().info("[DEBUG] Building text input");
        String initial = config.getString("initial", "");
        int width = config.getInt("width", 300);
        int maxLength = config.getInt("max_length", 0);
        boolean labelVisible = config.getBoolean("label_visible", true);

        //plugin.getLogger().info("[DEBUG] Text - initial:'" + initial + "', width:" + width + ", maxLength:" + maxLength + ", labelVisible:" + labelVisible);

        var builder = DialogInput.text(id, label)
                .initial(initial)
                .width(width)
                .labelVisible(labelVisible);

        if (maxLength > 0) {
            builder = builder.maxLength(maxLength);
            //plugin.getLogger().info("[DEBUG] Applied max length: " + maxLength);
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
            //plugin.getLogger().info("[DEBUG] Applied multiline - maxLines:" + maxLines + ", maxColumns:" + maxColumns);
        }

        return builder.build();
    }

    private DialogInput buildSingleOptionInput(String id, Component label, Configuration config) {
        //plugin.getLogger().info("[DEBUG] Building SingleOption input");
        int width = config.getInt("width", 300);
        boolean labelVisible = config.getBoolean("label_visible", true);

        List<SingleOptionDialogInput.OptionEntry> entries = new ArrayList<>();
        if (config.contains("entries")) {
            List<?> entryList = config.getList("entries");
            if (entryList != null) {
                for (Object entry : entryList) {
                    if (entry instanceof String textValue) {
                        // Convert each string to a proper OptionEntry
                        // The create method needs 3 parameters: id, displayText, isInitiallySelected
                        Component optionText = DialogConfigManager.parseFormattedText(textValue);
                        entries.add(SingleOptionDialogInput.OptionEntry.create(textValue, optionText, false));
                    }
                }
            }
            //plugin.getLogger().info("[DEBUG] Applied entries: " + entries);
        }

        var builder = DialogInput.singleOption(id, label, entries)
                .width(width)
                .labelVisible(labelVisible);

        return builder.build();
    }
}