
package io.github.devbd1.cubDialogs.dialog.types;

import io.papermc.paper.registry.data.dialog.type.DialogType;
import org.bukkit.configuration.ConfigurationSection;

public interface TypeInterface {
    DialogType buildDialogType(ConfigurationSection config);
    String getTypeName();
}
