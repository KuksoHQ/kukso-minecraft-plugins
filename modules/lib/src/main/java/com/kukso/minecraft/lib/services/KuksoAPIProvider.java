package com.kukso.minecraft.lib.services;

public class KuksoAPIProvider {

    private static KuksoAPI instance;

    public static void register(KuksoAPI api) {
        if (instance != null && instance != api) {
            throw new IllegalStateException("KuksoAPI already registered");
        }
        instance = api;
    }

    public static void unregister(KuksoAPI api) {
        if (instance == api) {
            instance = null;
        }
    }

    public static KuksoAPI get() {
        if (instance == null) throw new IllegalStateException("KuksoAPI is not available yet");
        return instance;
    }

    public static boolean isAvailable() {
        return instance != null;
    }
}
