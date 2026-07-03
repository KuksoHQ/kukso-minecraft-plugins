package com.kukso.minecraft.dialogs.addons.expconfig;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ExpConfigValuesTest {
    @Test
    void fromClampsLevelAndExperienceToSafeRanges() {
        ExpConfigValues values = ExpConfigValues.from(-4.5f, 140.0f);

        assertEquals(0, values.levels());
        assertEquals(100.0f, values.experiencePercent(), 0.0001f);
    }

    @Test
    void fromPreservesListenerIntegerTruncationForLevels() {
        ExpConfigValues values = ExpConfigValues.from(12.9f, 33.5f);

        assertEquals(12, values.levels());
        assertEquals(33.5f, values.experiencePercent(), 0.0001f);
    }

    @Test
    void fromReturnsNullWhenRequiredValuesAreMissing() {
        assertNull(ExpConfigValues.from(null, 25.0f));
        assertNull(ExpConfigValues.from(5.0f, null));
    }
}
