package io.github.devbd1.cubDialogs.features.dialog;

import io.papermc.paper.dialog.Dialog;
import org.bukkit.command.CommandSender;

public class DialogBuilder {
    /**
     * Builds and shows a dialog using config-defined dialogs.
     * Usage: /cubDialogs open <dialogId>
     * If no id is provided, falls back to "template_confirmation_type".
     */
    public boolean build(CommandSender sender, String[] args) {
        final String dialogId = (args != null && args.length > 0) ? args[0] : "template_confirmation_type";

        Dialog dialog = DialogConfigManager.buildDialog(dialogId);
        if (dialog == null) {
            sender.sendMessage("Could not build dialog: " + dialogId + " (check dialogs folder.)");
            return false;
        }

        sender.showDialog(dialog);
        return true;
    }
}