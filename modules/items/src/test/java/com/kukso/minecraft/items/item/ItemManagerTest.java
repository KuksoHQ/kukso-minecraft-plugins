package com.kukso.minecraft.items.item;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemManagerTest {
    @Test
    void colorTranslatesLegacyCodesForDisplayNames() {
        assertEquals("\u00A7aReward \u00A7lKey", ItemManager.color("&aReward &lKey"));
    }

    @Test
    void colorKeepsNullDisplayNameNull() {
        assertNull(ItemManager.color((String) null));
    }

    @Test
    void colorTranslatesLoreLinesInOrder() {
        List<String> colored = ItemManager.color(List.of("&7Line one", "&bLine two"));

        assertEquals(List.of("\u00A77Line one", "\u00A7bLine two"), colored);
    }
}
