package com.kukso.minecraft.dialogs.addons.expconfig;

import com.kukso.minecraft.dialogs.API.DialogActionContext;
import com.kukso.minecraft.dialogs.API.DialogActionListener;
import com.kukso.minecraft.dialogs.API.DialogActionRegistry;
import com.kukso.minecraft.dialogs.API.DialogKey;
import com.kukso.minecraft.dialogs.API.KuksoDialogsAPI;
import com.kukso.minecraft.dialogs.API.Registration;
import com.kukso.minecraft.dialogs.API.addon.AddonContext;
import com.kukso.minecraft.dialogs.API.addon.KuksoDialogsAddon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class ExpConfigAddon implements KuksoDialogsAddon {
    private static final DialogKey EXP_CONFIG_KEY = DialogKey.parse("kukso:exp_config/confirm");

    private final List<Registration> registrations = new ArrayList<>();
    private Logger logger;

    @Override
    public String id() {
        return "exp-config";
    }

    @Override
    public String version() {
        return "1.0.0";
    }

    @Override
    public void onEnable(AddonContext context) {
        this.logger = context.logger();
        logger.info("Initializing ExpConfig addon");

        String apiVersion = context.apiVersion();
        logger.info("Using KuksoDialogs API version: " + apiVersion);

        DialogActionRegistry registry = KuksoDialogsAPI.get();
        Registration registration = registry.register(EXP_CONFIG_KEY, createExpConfigListener());
        registrations.add(registration);

        logger.info("Registered experience config handler for key: " + EXP_CONFIG_KEY);
    }

    @Override
    public void onDisable() {
        for (Registration registration : registrations) {
            try {
                if (registration.isActive()) {
                    registration.unregister();
                }
            } catch (Exception e) {
                logger.warning("Error unregistering listener: " + e.getMessage());
            }
        }
        registrations.clear();

        logger.info("ExpConfig addon disabled");
    }

    private DialogActionListener createExpConfigListener() {
        return (DialogActionContext context) -> {
            Float levelValue = context.payload().getFloat("level");
            Float experienceValue = context.payload().getFloat("experience");
            String playerName = context.payload().getText("player_name");

            ExpConfigValues values = ExpConfigValues.from(levelValue, experienceValue);
            if (values == null) {
                context.reply("Missing level or experience values");
                return;
            }

            int levels = values.levels();
            float expPercent = values.experiencePercent();

            UUID senderId = context.playerId();
            Player sender = Bukkit.getPlayer(senderId);
            if (sender == null) {
                return;
            }

            Player target;
            if (playerName != null && !playerName.isBlank()) {
                target = Bukkit.getPlayerExact(playerName);
                if (target == null) {
                    context.reply("Player '" + playerName + "' is not online.");
                    return;
                }
            } else {
                target = sender;
            }

            target.setLevel(levels);
            target.setExp(expPercent / 100f);

            if (target.equals(sender)) {
                context.reply("Set your level to " + levels + " and exp to " + expPercent + "%.");
            } else {
                context.reply("Set " + target.getName() + "'s level to " + levels + " and exp to " + expPercent + "%.");
                target.sendMessage("Your level was set to " + levels + " and exp to " + expPercent + "%.");
            }
        };
    }
}
