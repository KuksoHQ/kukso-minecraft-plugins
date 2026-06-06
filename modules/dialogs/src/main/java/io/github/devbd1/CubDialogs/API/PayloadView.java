package io.github.devbd1.CubDialogs.API;

/**
 * Read-only view of submitted input values from a dialog.
 * Implementations are provided by the plugin; consumers only read values.
 *
 * All getters return boxed types and may return null if the key is absent or not coercible.
 */
public interface PayloadView {

    /**
     * @param key input key
     * @return String value or null
     */
    String getText(String key);

    /**
     * @param key input key
     * @return Integer value or null
     */
    Integer getInt(String key);

    /**
     * @param key input key
     * @return Float value or null
     */
    Float getFloat(String key);

    /**
     * @param key input key
     * @return Boolean value or null
     */
    Boolean getBoolean(String key);

    /**
     * Checks if a value is present for the given key (regardless of type).
     * @param key input key
     * @return true if present
     */
    default boolean has(String key) {
        return getText(key) != null
            || getInt(key) != null
            || getFloat(key) != null
            || getBoolean(key) != null;
    }
}
