package io.github.devbd1.expConfigAddon;

import io.github.devbd1.CubDialogs.API.CubDialogsAPI;
import io.github.devbd1.CubDialogs.API.DialogActionContext;
import io.github.devbd1.CubDialogs.API.DialogActionListener;
import io.github.devbd1.CubDialogs.API.DialogActionRegistry;
import io.github.devbd1.CubDialogs.API.DialogKey;
import io.github.devbd1.CubDialogs.API.Registration;
import io.github.devbd1.CubDialogs.API.addon.AddonContext;
import io.github.devbd1.CubDialogs.API.addon.CubDialogsAddon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class ExpConfigAddon implements CubDialogsAddon {
    private static final DialogKey EXP_CONFIG_KEY = DialogKey.parse("cublexcore:exp_config/confirm");
    
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
        
        // Verify API version compatibility if needed
        String apiVersion = context.apiVersion();
        logger.info("Using CubDialogs API version: " + apiVersion);
        
        // Register our listener
        DialogActionRegistry registry = CubDialogsAPI.get();
        Registration reg = registry.register(EXP_CONFIG_KEY, createExpConfigListener());
        registrations.add(reg);
        
        logger.info("Registered experience config handler for key: " + EXP_CONFIG_KEY);
    }
    
    @Override
    public void onDisable() {
        // Unregister all our listeners
        for (Registration reg : registrations) {
            try {
                if (reg.isActive()) {
                    reg.unregister();
                }
            } catch (Exception e) {
                logger.warning("Error unregistering listener: " + e.getMessage());
            }
        }
        registrations.clear();
        
        logger.info("ExpConfig addon disabled");
    }
    
    private DialogActionListener createExpConfigListener() {
        return (DialogActionContext ctx) -> {
            // Get the payload values
            Float levelF = ctx.payload().getFloat("level");
            Float expF = ctx.payload().getFloat("experience");
            String playerName = ctx.payload().getText("player_name");
            
            if (levelF == null || expF == null) {
                ctx.reply("Missing level or experience values");
                return;
            }
            
            int levels = Math.max(0, levelF.intValue());
            float expPercent = expF;
            
            // Get the player who triggered the action
            UUID senderId = ctx.playerId();
            Player sender = Bukkit.getPlayer(senderId);
            if (sender == null) {
                return; // Player went offline
            }
            
            // Determine target player (specified or self)
            Player target;
            if (playerName != null && !playerName.isBlank()) {
                target = Bukkit.getPlayerExact(playerName);
                if (target == null) {
                    ctx.reply("Player '" + playerName + "' is not online.");
                    return;
                }
            } else {
                target = sender;
            }
            
            // Apply the experience changes
            expPercent = Math.max(0f, Math.min(100f, expPercent));
            float exp = expPercent / 100f;
            
            target.setLevel(levels);
            target.setExp(exp);
            
            // Send feedback messages
            if (target.equals(sender)) {
                ctx.reply("Set your level to " + levels + " and exp to " + expPercent + "%.");
            } else {
                ctx.reply("Set " + target.getName() + "'s level to " + levels + " and exp to " + expPercent + "%.");
                target.sendMessage("Your level was set to " + levels + " and exp to " + expPercent + "%.");
            }
        };
    }
}
