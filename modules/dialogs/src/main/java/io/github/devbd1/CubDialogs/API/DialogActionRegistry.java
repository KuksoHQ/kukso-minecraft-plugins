package io.github.devbd1.CubDialogs.API;

/**
 * Registry for dialog custom action handlers.
 * Implemented by the plugin; consumers use this to register/unregister listeners for a key.
 */
public interface DialogActionRegistry {

    /**
     * Registers a listener for the given namespaced key.
     * If multiple listeners are registered for the same key, all will be invoked.
     *
     * @param key      the namespaced dialog action key
     * @param listener the listener to invoke when the key is triggered
     * @return a Registration handle that can be used to unregister the listener
     * @throws NullPointerException if key or listener is null
     */
    Registration register(DialogKey key, DialogActionListener listener);

    /**
     * Unregisters all listeners for the given key.
     *
     * @param key the namespaced dialog action key
     * @return number of listeners removed
     * @throws NullPointerException if key is null
     */
    int unregisterAll(DialogKey key);

    /**
     * Removes all listeners from this registry.
     * Intended for plugin shutdown.
     *
     * @return total number of listeners removed
     */
    int clear();
    
    /**
     * Executes all registered listeners for the given context.
     * This is used internally by the dialog event bridge.
     *
     * @param context the context to pass to listeners
     */
    void executeListeners(DialogActionContext context);
}
