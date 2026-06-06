package io.github.devbd1.CubDialogs.dialog;

import io.github.devbd1.CubDialogs.API.CubDialogsAPI;
import io.github.devbd1.CubDialogs.API.DialogActionContext;
import io.github.devbd1.CubDialogs.API.DialogKey;
import io.github.devbd1.CubDialogs.API.PayloadView;
import io.papermc.paper.connection.PlayerGameConnection;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Bridges Paper's custom click events to our DialogActionRegistry.
 */
public class DialogActionBridge implements Listener {
    private final Plugin plugin;
    private final Logger logger;
    
    public DialogActionBridge(Plugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    
    @EventHandler
    public void onCustomClick(PlayerCustomClickEvent event) {
        if (!CubDialogsAPI.isAvailable()) {
            logger.warning("API not available, skipping custom click event");
            return;
        }
        
        // Convert Paper's Key to our DialogKey
        Key identifier = event.getIdentifier();
        String namespace = identifier.namespace();
        String value = identifier.value();
        
        try {
            DialogKey dialogKey = DialogKey.of(namespace, value);
            
            // Get the response view (payload)
            DialogResponseView responseView = event.getDialogResponseView();
            if (responseView == null) {
                logger.warning("No dialog response view for key: " + dialogKey);
                return;
            }
            
            // Create PayloadView adapter
            PayloadView payloadView = new PaperPayloadViewAdapter(responseView);
            
            // Create context
            UUID playerId = null;
            String playerName = null;
            if (event.getCommonConnection() instanceof PlayerGameConnection conn) {
                Player player = conn.getPlayer();
                playerId = player.getUniqueId();
                playerName = player.getName();
            }
            
            if (playerId == null) {
                logger.warning("Could not get player from connection for key: " + dialogKey);
                return;
            }
            
            SimpleDialogActionContext context = new SimpleDialogActionContext(
                    dialogKey,
                    payloadView,
                    playerId,
                    playerName,
                    plugin
            );
            
            // Notify the registry
            CubDialogsAPI.get().register(dialogKey, ctx -> {});

            // Dispatch the event to all registered listeners
            try {
                CubDialogsAPI.get().executeListeners(context);
            } catch (Exception e) {
                logger.severe("Error executing listeners for key " + dialogKey + ": " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            logger.warning("Failed to process custom click for " + namespace + ":" + value + 
                    ": " + e.getMessage());
        }
    }
    
    /**
     * Adapter for Paper's DialogResponseView to our PayloadView.
     */
    private static class PaperPayloadViewAdapter implements PayloadView {
        private final DialogResponseView view;
        
        PaperPayloadViewAdapter(DialogResponseView view) {
            this.view = view;
        }
        
        @Override
        public String getText(String key) {
            return view.getText(key);
        }
        
        @Override
        public Integer getInt(String key) {
            Float f = view.getFloat(key);
            return f != null ? f.intValue() : null;
        }
        
        @Override
        public Float getFloat(String key) {
            return view.getFloat(key);
        }
        
        @Override
        public Boolean getBoolean(String key) {
            return view.getBoolean(key);
        }
    }
    
    /**
     * Simple implementation of DialogActionContext.
     */
    private static class SimpleDialogActionContext implements DialogActionContext {
        private final DialogKey key;
        private final PayloadView payload;
        private final UUID playerId;
        private final String playerName;
        private final Plugin plugin;
        
        SimpleDialogActionContext(DialogKey key, PayloadView payload, UUID playerId, 
                                 String playerName, Plugin plugin) {
            this.key = key;
            this.payload = payload;
            this.playerId = playerId;
            this.playerName = playerName;
            this.plugin = plugin;
        }
        
        @Override
        public DialogKey key() {
            return key;
        }
        
        @Override
        public PayloadView payload() {
            return payload;
        }
        
        @Override
        public UUID playerId() {
            return playerId;
        }
        
        @Override
        public Optional<String> playerName() {
            return Optional.ofNullable(playerName);
        }

        @Override
        public void reply(String message) {
            // Ensure this runs on the main thread
            if (Bukkit.isPrimaryThread()) {
                sendMessage(message);
            } else {
                final String finalMessage = message; // Create final copy for lambda
                Bukkit.getScheduler().runTask(plugin, () -> sendMessage(finalMessage));
            }
        }

        private void sendMessage(String message) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                player.sendMessage(message);
            }
        }

    }
}
