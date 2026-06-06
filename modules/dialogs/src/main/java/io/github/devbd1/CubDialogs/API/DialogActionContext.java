package io.github.devbd1.CubDialogs.API;

import java.util.Optional;
import java.util.UUID;

/**
 * Context passed to a DialogActionListener when a custom dialog key is triggered.
 * Platform (Paper/Bukkit) details are hidden; only neutral data is exposed.
 *
 * Implemented by the plugin; consumers just read values and reply.
 */
public interface DialogActionContext {

    /**
     * The namespaced custom action key that triggered this handler.
     */
    DialogKey key();

    /**
     * The submitted input values for this action.
     */
    PayloadView payload();

    /**
     * UUID of the player who triggered the action.
     */
    UUID playerId();

    /**
     * Optional name of the player who triggered the action.
     */
    Optional<String> playerName();

    /**
     * Send a message back to the triggering player.
     * Implementations ensure this runs safely on the server thread.
     */
    void reply(String message);
}
