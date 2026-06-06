package io.github.devbd1.CubDialogs.API;

/**
 * Handle returned when registering a DialogActionListener.
 * Allows later unregistration.
 */
public interface Registration {

    /**
     * Unregisters this listener from the registry.
     * Calling multiple times should be safe (no-op after first).
     */
    void unregister();

    /**
     * @return true if this registration is still active, false if it has been unregistered.
     */
    boolean isActive();
}
