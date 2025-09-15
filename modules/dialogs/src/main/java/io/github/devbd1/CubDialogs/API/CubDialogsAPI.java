package io.github.devbd1.CubDialogs.API;

import java.util.Objects;

/**
 * Entry point for CubDialogs' public API.
 * Consumers call CubDialogsAPI.get() to obtain the registry for registering action listeners.
 *
 * The API version is exposed separately from the plugin version, allowing the implementation
 * to evolve without forcing an API change (and vice versa).
 */
public final class CubDialogsAPI {

    private static volatile DialogActionRegistry REGISTRY;
    private static volatile String API_VERSION = "1.0.0";

    private CubDialogsAPI() {
        // no instances
    }

    /**
     * Returns the global DialogActionRegistry.
     *
     * @throws IllegalStateException if the API has not been bootstrapped by the plugin yet
     */
    public static DialogActionRegistry get() {
        DialogActionRegistry reg = REGISTRY;
        if (reg == null) {
            throw new IllegalStateException("CubDialogs API is not available yet. Is the plugin enabled?");
        }
        return reg;
    }

    /**
     * @return true if the API is available (plugin has bootstrapped it), false otherwise
     */
    public static boolean isAvailable() {
        return REGISTRY != null;
    }

    /**
     * @return the semantic API version string. This may or may not match the plugin version.
     */
    public static String getApiVersion() {
        return API_VERSION;
    }

    /**
     * Initializes the API with a registry implementation and version.
     * Intended to be called by the plugin during startup exactly once.
     *
     * @param registry   the registry implementation
     * @param apiVersion the API version string (e.g., "1.0.0"); if null/blank, defaults to "unknown"
     * @throws IllegalStateException if the API is already initialized
     */
    public static void bootstrap(DialogActionRegistry registry, String apiVersion) {
        Objects.requireNonNull(registry, "registry");
        synchronized (CubDialogsAPI.class) {
            if (REGISTRY != null) {
                throw new IllegalStateException("CubDialogs API is already initialized");
            }
            REGISTRY = registry;
            API_VERSION = (apiVersion != null && !apiVersion.isBlank()) ? apiVersion : "unknown";
        }
    }

    /**
     * Shuts down the API. Intended for internal/plugin use during disable.
     */
    public static void shutdown() {
        synchronized (CubDialogsAPI.class) {
            REGISTRY = null;
            API_VERSION = "unknown";
        }
    }
}