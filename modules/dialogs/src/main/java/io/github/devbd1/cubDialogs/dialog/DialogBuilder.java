package io.github.devbd1.cubDialogs.dialog;

import io.papermc.paper.dialog.Dialog;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DialogBuilder {
    //private DialogBuilder() {}

    public boolean build(CommandSender sender, String[] args) {
        final String dialogId = (args != null && args.length > 0) ? args[0] : "feedback_form";

        Dialog dialog = DialogConfigManager.buildDialog(dialogId);
        if (dialog == null) {
            sender.sendMessage("Could not build dialog: " + dialogId + " (check dialogs folder.)");
            return false;
        }

        sender.showDialog(dialog);
        return true;
    }

    public boolean build(Player player, String dialogId) {
        if (dialogId == null || dialogId.isBlank()) {
            player.sendMessage("§cDialog ID cannot be null or blank!");
            return false;
        }

        Dialog dialog = DialogConfigManager.buildDialog(dialogId);
        if (dialog == null) {
            player.sendMessage("§cCould not build dialog: " + dialogId + " (check dialogs folder.)");
            return false;
        }

        player.showDialog(dialog);
        return true;
    }
}