package com.kukso.minecraft.lib.modules;

import com.kukso.minecraft.lib.Main;
import com.kukso.minecraft.lib.modules.text.PacketTranslator;
import com.kukso.minecraft.lib.services.KuksoAPI;
import com.kukso.minecraft.lib.modules.logger.LoggingManager;

public class ModuleRegistrar {
    private ModuleRegistrar() {
    }
    public static void register(Main plugin, KuksoAPI api, LoggingManager logger) {

        // ** ProtocolLib packetâ€translation hook **
        PacketTranslator.init(plugin, api, logger);
    }
}
