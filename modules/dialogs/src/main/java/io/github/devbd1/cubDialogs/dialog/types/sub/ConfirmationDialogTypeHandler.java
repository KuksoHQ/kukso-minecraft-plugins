package io.github.devbd1.cubDialogs.dialog.types.sub;

import io.github.devbd1.cubDialogs.dialog.types.TypeInterface;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;

import static io.github.devbd1.cubDialogs.dialog.DialogConfigManager.buildButton;
import static io.github.devbd1.cubDialogs.dialog.DialogConfigManager.namedOrHex;

public class ConfirmationDialogTypeHandler implements TypeInterface {

    @Override
    public DialogType buildDialogType(ConfigurationSection config) {
        ConfigurationSection buttonsSec = config != null ? config.getConfigurationSection("buttons") : null;

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

    @Override
    public String getTypeName() {
        return "confirmation";
    }
}