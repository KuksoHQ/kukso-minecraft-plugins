package com.kukso.minecraft.dialogs.API;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DialogKeyTest {
    @Test
    void parseTrimsLowercasesAndSplitsNamespacedKeys() {
        DialogKey key = DialogKey.parse(" Kukso:Exp_Config/Confirm ");

        assertAll(
                () -> assertEquals("kukso", key.namespace()),
                () -> assertEquals("exp_config/confirm", key.value()),
                () -> assertEquals("kukso:exp_config/confirm", key.asString()),
                () -> assertEquals(key, DialogKey.of("kukso", "exp_config/confirm"))
        );
    }

    @Test
    void parseRejectsMissingPartsAndIllegalCharacters() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> DialogKey.parse("missing_value:")),
                () -> assertThrows(IllegalArgumentException.class, () -> DialogKey.parse(":missing_namespace")),
                () -> assertThrows(IllegalArgumentException.class, () -> DialogKey.parse("kukso:bad value"))
        );
    }
}
