package io.github.devbd1.cubDialogs.dialog.types.sub;

import io.github.devbd1.cubDialogs.dialog.types.TypeInterface;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;

import static io.github.devbd1.cubDialogs.dialog.DialogConfigManager.buildButton;
import static io.github.devbd1.cubDialogs.dialog.DialogConfigManager.namedOrHex;

public class NoticeDialogTypeHandler implements TypeInterface {

    @Override
    public DialogType buildDialogType(ConfigurationSection config) {
        ConfigurationSection buttonSec = config != null ? config.getConfigurationSection("exit_button") : null;

        if (buttonSec == null) {
            // Fallback button
            ActionButton okButton = ActionButton.create(
                    Component.text("OK", namedOrHex("blue")),
                    Component.text("Click to acknowledge."),
                    100,
                    null
            );
            return DialogType.notice(okButton);
        }

        ActionButton okButton = buildButton(buttonSec,
                "OK", "Click to acknowledge.", "blue", 100);

        return DialogType.notice(okButton);
    }

    @Override
    public String getTypeName() {
        return "notice";
    }
}
