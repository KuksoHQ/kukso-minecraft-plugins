package com.kukso.minecraft.lib.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorManagerTest {
    @Test
    void applyColorFormattingHandlesHexGradientAndLegacyCodes() {
        String input = "&aHi #ff00aa <gradient:#000000:#ffffff>AB</gradient>";

        String expected = "\u00A7aHi "
                + minecraftHex("ff00aa")
                + " "
                + minecraftHex("000000") + "A"
                + minecraftHex("ffffff") + "B";

        assertEquals(expected, ColorManager.applyColorFormatting(input));
    }

    @Test
    void applyHexColorsLeavesNonHexTextUnchanged() {
        assertEquals("Use #ffaa but not #xyzxyz", ColorManager.applyHexColors("Use #ffaa but not #xyzxyz"));
    }

    private static String minecraftHex(String hex) {
        StringBuilder out = new StringBuilder("\u00A7x");
        for (char c : hex.toCharArray()) {
            out.append('\u00A7').append(c);
        }
        return out.toString();
    }
}
