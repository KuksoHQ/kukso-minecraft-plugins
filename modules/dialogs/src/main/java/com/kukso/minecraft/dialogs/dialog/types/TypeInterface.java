
package com.kukso.minecraft.dialogs.dialog.types;

import io.papermc.paper.registry.data.dialog.type.DialogType;
import org.bukkit.configuration.ConfigurationSection;

public interface TypeInterface {
    DialogType buildDialogType(ConfigurationSection config);
    String getTypeName();
}
