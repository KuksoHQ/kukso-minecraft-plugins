package io.github.devbd1.CubDialogs.API;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Namespaced identifier for a dialog custom event key.
 * Example: "cublexcore:exp_config/confirm"
 *
 * namespace: "cublexcore"
 * value:     "exp_config/confirm"
 */
public final class DialogKey {

    private static final Pattern NAMESPACE_PATTERN = Pattern.compile("[a-z0-9._-]+");
    private static final Pattern VALUE_PATTERN = Pattern.compile("[a-z0-9._/\\-]+");

    private final String namespace;
    private final String value;
    private final String full; // cached "namespace:value"

    private DialogKey(String namespace, String value) {
        this.namespace = namespace;
        this.value = value;
        this.full = namespace + ":" + value;
    }

    /**
     * Creates a DialogKey from separate namespace and value parts.
     */
    public static DialogKey of(String namespace, String value) {
        Objects.requireNonNull(namespace, "namespace");
        Objects.requireNonNull(value, "value");
        String ns = namespace.trim().toLowerCase();
        String val = value.trim().toLowerCase();
        if (!isValidNamespace(ns)) {
            throw new IllegalArgumentException("Invalid namespace: " + namespace);
        }
        if (!isValidValue(val)) {
            throw new IllegalArgumentException("Invalid value: " + value);
        }
        return new DialogKey(ns, val);
    }

    /**
     * Parses a full key string in the form "namespace:value".
     */
    public static DialogKey parse(String fullKey) {
        Objects.requireNonNull(fullKey, "fullKey");
        String s = fullKey.trim().toLowerCase();
        int idx = s.indexOf(':');
        if (idx <= 0 || idx == s.length() - 1) {
            throw new IllegalArgumentException("Expected 'namespace:value' format: " + fullKey);
        }
        String ns = s.substring(0, idx);
        String val = s.substring(idx + 1);
        return of(ns, val);
    }

    public String namespace() {
        return namespace;
    }

    public String value() {
        return value;
    }

    /**
     * Full string representation in the form "namespace:value".
     */
    public String asString() {
        return full;
    }

    @Override
    public String toString() {
        return full;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DialogKey that)) return false;
        return namespace.equals(that.namespace) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, value);
    }

    private static boolean isValidNamespace(String ns) {
        return !ns.isEmpty() && NAMESPACE_PATTERN.matcher(ns).matches();
    }

    private static boolean isValidValue(String val) {
        return !val.isEmpty() && VALUE_PATTERN.matcher(val).matches();
    }
}
