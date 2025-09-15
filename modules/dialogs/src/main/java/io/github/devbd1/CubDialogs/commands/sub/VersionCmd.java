package io.github.devbd1.CubDialogs.commands.sub;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.devbd1.CubDialogs.commands.CmdConfig;
import io.github.devbd1.CubDialogs.commands.CmdInterface;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class VersionCmd implements CmdInterface {
    String CMD_NAME = "version";

    @Override
    public String getName() {
        return CMD_NAME;
    }

    @Override
    public List<String> getAliases() {
        return CmdConfig.getAliases(CMD_NAME);
    }

    @Override
    public List<String> getPermissions() {
        return CmdConfig.getPermissions(CMD_NAME);
    }

    @Override
    public String getDescription() {
        return "Shows plugin version, API version, and checks the latest tag on GitHub.";
    }

    @Override
    public String getUsage() {
        return CmdInterface.super.getUsage();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return List.of();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("CubDialogs");
        String pluginVersion = plugin != null ? plugin.getDescription().getVersion() : "unknown";
        //String apiVersion = CubDialogsAPI.getApiVersion();

        sender.sendMessage("CubDialogs version: " + pluginVersion);
        //sender.sendMessage("API version: " + apiVersion);
        sender.sendMessage("Checking latest version...");

        var bukkitScheduler = Bukkit.getScheduler();
        JavaPlugin owningPlugin = JavaPlugin.getProvidingPlugin(VersionCmd.class);

        bukkitScheduler.runTaskAsynchronously(owningPlugin, () -> {
            try {
                String latestTag = fetchLatestTag();
                if (latestTag == null) {
                    sender.sendMessage("Could not determine the latest version at this time.");
                    return;
                }

                String latestNorm = normalizeVersion(latestTag);
                String currentNorm = normalizeVersion(pluginVersion);

                int cmp = compareVersions(currentNorm, latestNorm);
                if (cmp < 0) {
                    sender.sendMessage("Update available: " + latestTag + " (you have " + pluginVersion + ")");
                    sender.sendMessage("Get it at: https://github.com/CublexLabs/CubDialogs/tags");
                } else if (cmp == 0) {
                    sender.sendMessage("You are up to date (" + pluginVersion + ").");
                } else {
                    sender.sendMessage("You are ahead (" + pluginVersion + "), latest tag is " + latestTag + ".");
                }
            } catch (Exception ex) {
                sender.sendMessage("Failed to check latest version: " + ex.getMessage());
            }
        });

        return true;
    }

    private static String fetchLatestTag() throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        // Use GitHub API to list tags (first one is the latest by default order)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/repos/CublexLabs/CubDialogs/tags"))
                .timeout(Duration.ofSeconds(8))
                .header("Accept", "application/vnd.github+json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) return null;

        JsonElement root = JsonParser.parseString(response.body());
        if (!root.isJsonArray()) return null;

        JsonArray arr = root.getAsJsonArray();
        if (arr.isEmpty()) return null;

        JsonElement first = arr.get(0);
        if (!first.isJsonObject()) return null;

        JsonElement nameEl = first.getAsJsonObject().get("name");
        return nameEl != null ? nameEl.getAsString() : null;
    }

    private static String normalizeVersion(String v) {
        if (v == null) return "0.0.0";
        v = v.trim();
        if (v.startsWith("v") || v.startsWith("V")) v = v.substring(1);
        // Keep only major.minor.patch prefix for comparison
        String[] parts = v.split("[^0-9.]+", 2);
        return parts.length > 0 ? parts[0] : v;
    }

    private static int compareVersions(String a, String b) {
        String[] as = a.split("\\.");
        String[] bs = b.split("\\.");
        int len = Math.max(as.length, bs.length);
        for (int i = 0; i < len; i++) {
            int ai = i < as.length ? parseIntSafe(as[i]) : 0;
            int bi = i < bs.length ? parseIntSafe(bs[i]) : 0;
            if (ai != bi) return Integer.compare(ai, bi);
        }
        return 0;
    }

    private static int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }
}
