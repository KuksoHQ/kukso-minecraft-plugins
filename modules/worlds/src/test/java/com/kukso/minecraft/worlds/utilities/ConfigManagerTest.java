package com.kukso.minecraft.worlds.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigManagerTest {
    @Test
    void validateKeyAcceptsDottedDashedAndUnderscoredConfigPaths() {
        assertDoesNotThrow(() -> ConfigManager.validateKey("commands.reload"));
        assertDoesNotThrow(() -> ConfigManager.validateKey("world-border.size"));
        assertDoesNotThrow(() -> ConfigManager.validateKey("default_world"));
    }

    @Test
    void validateKeyRejectsNullBlankSlashAndSpaceSeparatedKeys() {
        assertThrows(IllegalArgumentException.class, () -> ConfigManager.validateKey(null));
        assertThrows(IllegalArgumentException.class, () -> ConfigManager.validateKey(""));
        assertThrows(IllegalArgumentException.class, () -> ConfigManager.validateKey("commands/reload"));
        assertThrows(IllegalArgumentException.class, () -> ConfigManager.validateKey("commands reload"));
    }
}
