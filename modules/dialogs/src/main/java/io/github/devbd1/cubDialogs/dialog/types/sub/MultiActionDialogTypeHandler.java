package io.github.devbd1.cubDialogs.dialog.types.sub;

import io.github.devbd1.cubDialogs.dialog.types.TypeInterface;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

import static io.github.devbd1.cubDialogs.dialog.DialogConfigManager.buildButton;
import static io.github.devbd1.cubDialogs.dialog.DialogConfigManager.namedOrHex;

public class MultiActionDialogTypeHandler implements TypeInterface {

    @Override
    public DialogType buildDialogType(ConfigurationSection config) {
        List<ActionButton> buttons = new ArrayList<>();

        if (config != null) {
            ConfigurationSection buttonsSection = config.getConfigurationSection("buttons");
            if (buttonsSection != null) {
                // Process each button defined in the configuration
                for (String buttonKey : buttonsSection.getKeys(false)) {
                    ConfigurationSection buttonConfig = buttonsSection.getConfigurationSection(buttonKey);
                    if (buttonConfig != null) {
                        ActionButton button = buildButton(buttonConfig,
                                "Action", "Click to perform action.", "blue", 100);
                        buttons.add(button);
                    }
                }
            }

            if (config.isList("buttons")) {
                List<?> buttonsList = config.getList("buttons");
                if (buttonsList != null) {
                    for (Object buttonData : buttonsList) {
                        if (buttonData instanceof java.util.Map<?, ?> buttonMap) {
                            // Convert map to temporary configuration section
                            org.bukkit.configuration.MemoryConfiguration tempConfig = new org.bukkit.configuration.MemoryConfiguration();

                            for (java.util.Map.Entry<?, ?> entry : buttonMap.entrySet()) {
                                String key = entry.getKey().toString();
                                Object value = entry.getValue();

                                // Special handling for nested maps (like action)
                                if (value instanceof java.util.Map<?, ?> nestedMap && key.equals("action")) {
                                    // Create a nested configuration section for action
                                    org.bukkit.configuration.MemoryConfiguration actionSection = new org.bukkit.configuration.MemoryConfiguration();
                                    for (java.util.Map.Entry<?, ?> actionEntry : nestedMap.entrySet()) {
                                        actionSection.set(actionEntry.getKey().toString(), actionEntry.getValue());
                                    }
                                    tempConfig.set(key, actionSection);
                                } else {
                                    tempConfig.set(key, value);
                                }
                            }

                            ActionButton button = buildButton(tempConfig, "Action", "Click to perform action.", "blue", 100);
                            buttons.add(button);
                        }
                    }
                }
            }
        }

        // If no buttons were configured, provide default buttons
        if (buttons.isEmpty()) {
            ActionButton action1 = ActionButton.create(
                    Component.text("These are", namedOrHex("blue")),
                    Component.text("You should configure buttons in this dialog configuration."),
                    100,
                    null
            );
            ActionButton action2 = ActionButton.create(
                    Component.text("Default", namedOrHex("green")),
                    Component.text("You should configure buttons in this dialog configuration."),
                    100,
                    null
            );
            ActionButton cancel = ActionButton.create(
                    Component.text("Buttons", namedOrHex("red")),
                    Component.text("You should configure buttons in this dialog configuration."),
                    100,
                    null
            );
            buttons.add(action1);
            buttons.add(action2);
            buttons.add(cancel);
        }

        // Get columns from config or default to 2
        int columns = config != null ? config.getInt("columns", 2) : 2;

        // Get exit action if configured (optional)
        ActionButton exitAction = null;
        if (config != null && config.isConfigurationSection("exit_button")) {
            ConfigurationSection exitActionSection = config.getConfigurationSection("exit_button");
            exitAction = buildButton(exitActionSection, "Action", "Click to perform action.", "blue", 100);
        }

        return DialogType.multiAction(buttons, exitAction, columns);
    }

    @Override
    public String getTypeName() {
        return "multi_action";
    }
}
