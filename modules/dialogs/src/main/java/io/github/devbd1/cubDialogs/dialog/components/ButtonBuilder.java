package io.github.devbd1.cubDialogs.dialog.components;

import io.github.devbd1.cubDialogs.dialog.DialogConfigManager;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import static net.kyori.adventure.text.event.ClickEvent.changePage;

/**
 * Handles the creation of dialog buttons based on configuration data.
 */
public class ButtonBuilder {
    private final JavaPlugin plugin;

    public ButtonBuilder(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Builds an ActionButton from a configuration section.
     *
     * @param sec Configuration section containing button settings
     * @param defText Default text to use if not specified in configuration
     * @param defDesc Default description to use if not specified in configuration
     * @param defColor Default color to use if not specified in configuration
     * @param defWidth Default width to use if not specified in configuration
     * @return An ActionButton instance
     */
    public ActionButton buildButton(ConfigurationSection sec, String defText, String defDesc, String defColor, int defWidth) {
        if (sec == null) {
            return ActionButton.create(
                    Component.text(defText, DialogConfigManager.namedOrHex(defColor)),
                    Component.text(defDesc),
                    defWidth,
                    null
            );
        }

        String text = sec.getString("text", defText);
        String desc = sec.getString("description", defDesc);
        int width = sec.getInt("width", defWidth);

        DialogAction action = buildAction(sec.getConfigurationSection("action"));

        // Apply ColorManager formatting to button text and description
        String formattedText = io.github.devbd1.cubDialogs.utilities.ColorManager.applyColorFormatting(text);
        String formattedDesc = io.github.devbd1.cubDialogs.utilities.ColorManager.applyColorFormatting(desc);

        // Parse the formatted text using MiniMessage/Adventure
        Component textComponent = DialogConfigManager.parseFormattedText(formattedText);
        Component descComponent = DialogConfigManager.parseFormattedText(formattedDesc);

        return ActionButton.create(
                textComponent,
                descComponent,
                width,
                action
        );
    }

    /**
     * Builds a DialogAction from a configuration section.
     *
     * @param sec Configuration section containing action settings
     * @return A DialogAction instance or null if no action or unknown action type
     */
    private DialogAction buildAction(ConfigurationSection sec) {
        if (sec == null) return null;

        String type = sec.getString("type", "return").toLowerCase(java.util.Locale.ROOT);
        return switch (type) {
            case "close" -> {
                yield DialogAction.staticAction(ClickEvent.callback(audience -> {
                    if (audience instanceof org.bukkit.entity.Player player) {
                        player.closeInventory();
                    }
                }));
            }

            case "return" -> {
                yield DialogAction.staticAction(ClickEvent.callback(audience -> {
                    audience.closeDialog();
                }));
            }

            case "copy_to_clipboard" -> {
                String text = sec.getString("text");
                if (text == null || text.isBlank()) {
                    plugin.getLogger().warning("Missing text for copy_to_clipboard action");
                }
                yield DialogAction.staticAction(ClickEvent.copyToClipboard(text));
            }
            case "show_dialog" -> {
                String dialogId = sec.getString("id");
                if (dialogId == null || dialogId.isBlank()) {
                    plugin.getLogger().warning("Missing dialog ID for show_dialog action");
                    yield null;
                }

                // Check if dialog exists first
                if (!DialogConfigManager.hasDialog(dialogId)) {
                    plugin.getLogger().warning("Dialog ID does not exist: " + dialogId);
                    yield null;
                }

                // Build the dialog
                Dialog dialog = DialogConfigManager.buildDialog(dialogId);
                if (dialog == null) {
                    plugin.getLogger().warning("Failed to build dialog: " + dialogId);
                    yield null;
                }

                // Return the action without showing the dialog immediately
                yield DialogAction.staticAction(ClickEvent.showDialog(dialog));
            }

            case "open_url" -> {
                String url = sec.getString("url");
                if (url == null || url.isBlank()) {
                    plugin.getLogger().warning("Missing URL for open_url action");
                    yield null;
                }

                yield DialogAction.staticAction(ClickEvent.openUrl(url));
            }

            case "run_command" -> {
                String command = sec.getString("command");
                if (command == null || command.isBlank()) {
                    plugin.getLogger().warning("Missing command for run_command action");
                    yield null;
                }
                yield DialogAction.staticAction(ClickEvent.runCommand(command));
            }

            case "suggest_command" -> {
                String command = sec.getString("command");
                if (command == null || command.isBlank()) {
                    plugin.getLogger().warning("Missing command for suggest_command action");
                    yield null;
                }
                yield DialogAction.staticAction(ClickEvent.suggestCommand(command));
            }

            case "custom" -> {
                String key = sec.getString("key", null); // "papermc:user_input/confirm"
                yield DialogAction.customClick(Key.key(key), null);
            }

            case "null" -> null;

            default -> {
                plugin.getLogger().warning("Unsupported action type: " + type);
                yield null;
            }
        };
    }
}