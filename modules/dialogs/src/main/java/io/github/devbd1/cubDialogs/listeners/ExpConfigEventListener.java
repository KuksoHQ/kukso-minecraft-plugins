package io.github.devbd1.cubDialogs.listeners;

import io.papermc.paper.connection.PlayerGameConnection;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class ExpConfigEventListener implements Listener {
    private static final Key CONFIRM_ID = Key.key("cublexcore:exp_config/confirm");

    @EventHandler
    public void onCustomClick(PlayerCustomClickEvent event) {
        if (!event.getIdentifier().equals(CONFIRM_ID)) return;

        DialogResponseView view = event.getDialogResponseView();
        if (view == null) return;

        Float levelF = view.getFloat("level");
        Float expF = view.getFloat("experience");
        String playerName = view.getText("player_name");

        if (levelF == null || expF == null) return;

        int levels = Math.max(0, levelF.intValue());
        float expPercent = expF;

        if (event.getCommonConnection() instanceof PlayerGameConnection conn) {
            Player sender = conn.getPlayer();

            // Determine target player: specified name (if online) or the sender
            Player target;
            if (playerName != null && !playerName.isBlank()) {
                target = Bukkit.getPlayerExact(playerName);
                if (target == null) {
                    sender.sendMessage("Player '" + playerName + "' is not online.");
                    return;
                }
            } else {
                target = sender;
            }

            // Clamp experience percent to [0, 100] and convert to [0.0f, 1.0f]
            expPercent = Math.max(0f, Math.min(100f, expPercent));
            float exp = expPercent / 100f;

            target.setLevel(levels);
            target.setExp(exp);

            if (target.equals(sender)) {
                sender.sendMessage("Set your level to " + levels + " and exp to " + expPercent + "%.");
            } else {
                sender.sendMessage("Set " + target.getName() + "'s level to " + levels + " and exp to " + expPercent + "%.");
                target.sendMessage("Your level was set to " + levels + " and exp to " + expPercent + "%.");
            }
        }
    }
}