
package io.github.devbd1.CubDialogs.dialog;

import io.github.devbd1.CubDialogs.API.DialogActionContext;
import io.github.devbd1.CubDialogs.API.DialogActionListener;
import io.github.devbd1.CubDialogs.API.DialogActionRegistry;
import io.github.devbd1.CubDialogs.API.DialogKey;
import io.github.devbd1.CubDialogs.API.Registration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of DialogActionRegistry that maintains a map of listeners.
 */
public class SimpleDialogActionRegistry implements DialogActionRegistry {
    private final Map<DialogKey, List<RegisteredListener>> listeners = new ConcurrentHashMap<>();
    private final Logger logger;
    
    public SimpleDialogActionRegistry(Logger logger) {
        this.logger = logger;
    }
    
    @Override
    public Registration register(DialogKey key, DialogActionListener listener) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(listener, "listener");
        
        // Get or create the list for this key
        List<RegisteredListener> keyListeners = listeners.computeIfAbsent(
                key, k -> Collections.synchronizedList(new ArrayList<>())
        );
        
        // Create the registration
        RegisteredListener registration = new RegisteredListener(key, listener);
        keyListeners.add(registration);
        
        logger.info("Registered listener for key: " + key);
        return registration;
    }
    
    @Override
    public int unregisterAll(DialogKey key) {
        Objects.requireNonNull(key, "key");
        
        List<RegisteredListener> removed = listeners.remove(key);
        if (removed == null) {
            return 0;
        }
        
        // Mark all as inactive
        for (RegisteredListener listener : removed) {
            listener.active = false;
        }
        
        logger.info("Unregistered all listeners for key: " + key);
        return removed.size();
    }
    
    @Override
    public int clear() {
        int total = 0;
        
        // Count total listeners
        for (List<RegisteredListener> list : listeners.values()) {
            total += list.size();
            
            // Mark all as inactive
            for (RegisteredListener listener : list) {
                listener.active = false;
            }
        }
        
        listeners.clear();
        logger.info("Cleared all dialog action listeners: " + total + " total");
        return total;
    }

    /**
     * Execute all listeners registered for a given context.
     * Implementation of the public interface method.
     */
    @Override
    public void executeListeners(DialogActionContext context) {
        DialogKey key = context.key();
        List<RegisteredListener> keyListeners = listeners.get(key);

        if (keyListeners == null || keyListeners.isEmpty()) {
            logger.fine("No listeners registered for key: " + key);
            return;
        }

        // Create a copy to avoid concurrent modification
        List<RegisteredListener> copy = new ArrayList<>(keyListeners);

        for (RegisteredListener registration : copy) {
            if (!registration.active) {
                // Remove inactive listeners
                keyListeners.remove(registration);
                continue;
            }

            try {
                registration.listener.onAction(context);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error in dialog action listener for key " + key, e);
            }
        }
    }
    
    /**
     * Combined Registration and listener storage.
     */
    private class RegisteredListener implements Registration {
        private final DialogKey key;
        private final DialogActionListener listener;
        private volatile boolean active = true;
        
        RegisteredListener(DialogKey key, DialogActionListener listener) {
            this.key = key;
            this.listener = listener;
        }
        
        @Override
        public void unregister() {
            if (!active) {
                return;
            }
            
            active = false;
            
            List<RegisteredListener> keyListeners = listeners.get(key);
            if (keyListeners != null) {
                keyListeners.remove(this);
                
                // If the list is empty, remove the key entirely
                if (keyListeners.isEmpty()) {
                    listeners.remove(key);
                }
            }
        }
        
        @Override
        public boolean isActive() {
            return active;
        }
    }
}
