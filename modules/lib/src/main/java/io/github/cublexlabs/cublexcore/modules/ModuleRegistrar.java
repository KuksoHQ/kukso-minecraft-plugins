package io.github.cublexlabs.cublexcore.modules;

import io.github.cublexlabs.cublexcore.Main;
import io.github.cublexlabs.cublexcore.modules.text.PacketTranslator;
import io.github.cublexlabs.cublexcore.services.CorlexAPI;
import io.github.cublexlabs.cublexcore.modules.logger.LoggingManager;

public class ModuleRegistrar {
    private ModuleRegistrar() {
    }
    public static void register(Main plugin, CorlexAPI api, LoggingManager logger) {

        // ** ProtocolLib packet‐translation hook **
        PacketTranslator.init(plugin, api, logger);
    }
}
