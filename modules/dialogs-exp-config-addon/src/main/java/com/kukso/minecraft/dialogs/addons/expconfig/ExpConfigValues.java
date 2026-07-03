package com.kukso.minecraft.dialogs.addons.expconfig;

record ExpConfigValues(int levels, float experiencePercent) {
    static ExpConfigValues from(Float levelValue, Float experienceValue) {
        if (levelValue == null || experienceValue == null) {
            return null;
        }

        int levels = Math.max(0, levelValue.intValue());
        float experiencePercent = Math.max(0f, Math.min(100f, experienceValue));
        return new ExpConfigValues(levels, experiencePercent);
    }
}
