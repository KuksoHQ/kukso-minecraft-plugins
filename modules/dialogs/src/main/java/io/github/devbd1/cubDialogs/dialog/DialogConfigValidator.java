package io.github.devbd1.cubDialogs.dialog;

import io.github.devbd1.cubDialogs.utilities.ColorManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Validates dialog configurations for common issues and provides helpful warnings.
 */
public class DialogConfigValidator {
    
    /**
     * Represents a validation issue found in a dialog configuration
     */
    public static class ValidationIssue {
        public enum Severity { WARNING, ERROR, INFO }
        
        private final Severity severity;
        private final String dialogId;
        private final String inputId;
        private final String message;
        private final String suggestion;
        
        public ValidationIssue(Severity severity, String dialogId, String inputId, String message, String suggestion) {
            this.severity = severity;
            this.dialogId = dialogId;
            this.inputId = inputId;
            this.message = message;
            this.suggestion = suggestion;
        }
        
        // Getters
        public Severity getSeverity() { return severity; }
        public String getDialogId() { return dialogId; }
        public String getInputId() { return inputId; }
        public String getMessage() { return message; }
        public String getSuggestion() { return suggestion; }
    }
    
    /**
     * Validates all dialog configurations and returns a list of issues found
     */
    public static List<ValidationIssue> validateAllDialogs(JavaPlugin plugin) {
        List<ValidationIssue> issues = new ArrayList<>();
        
        File dialogsFolder = new File(plugin.getDataFolder(), "dialogs");
        if (!dialogsFolder.exists() || !dialogsFolder.isDirectory()) {
            return issues; // Empty list if no dialogs folder
        }
        
        File[] yamlFiles = dialogsFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
        if (yamlFiles == null) return issues;
        
        for (File yamlFile : yamlFiles) {
            String dialogId = yamlFile.getName().substring(0, yamlFile.getName().length() - 4);
            try {
                FileConfiguration config = YamlConfiguration.loadConfiguration(yamlFile);
                issues.addAll(validateDialog(dialogId, config));
            } catch (Exception e) {
                issues.add(new ValidationIssue(
                    ValidationIssue.Severity.ERROR,
                    dialogId,
                    null,
                    "Failed to load configuration: " + e.getMessage(),
                    "Check YAML syntax and file encoding"
                ));
            }
        }
        
        return issues;
    }
    
    /**
     * Validates a specific dialog configuration
     */
    public static List<ValidationIssue> validateDialog(String dialogId, ConfigurationSection config) {
        List<ValidationIssue> issues = new ArrayList<>();
        
        // Check for nested structure (old format warning)
        if (config.contains(dialogId)) {
            issues.add(new ValidationIssue(
                ValidationIssue.Severity.INFO,
                dialogId,
                null,
                "Dialog uses nested structure (dialog ID as root key)",
                "Consider moving content to root level for cleaner structure"
            ));
            config = config.getConfigurationSection(dialogId);
        }
        
        // Validate inputs
        List<?> inputsList = config.getList("inputs");
        if (inputsList != null) {
            for (int i = 0; i < inputsList.size(); i++) {
                Object inputData = inputsList.get(i);
                if (inputData instanceof Map<?, ?> inputMap) {
                    issues.addAll(validateInput(dialogId, inputMap, i));
                }
            }
        }
        
        // Validate buttons
        ConfigurationSection buttonsSection = config.getConfigurationSection("buttons");
        if (buttonsSection != null) {
            issues.addAll(validateButtons(dialogId, buttonsSection));
        }
        
        // Validate title
        if (!config.contains("title")) {
            issues.add(new ValidationIssue(
                ValidationIssue.Severity.WARNING,
                dialogId,
                null,
                "No title specified",
                "Add a 'title' field to improve user experience"
            ));
        }
        
        return issues;
    }
    
    /**
     * Validates a single input configuration
     */
    private static List<ValidationIssue> validateInput(String dialogId, Map<?, ?> inputMap, int index) {
        List<ValidationIssue> issues = new ArrayList<>();
        
        String inputId = (String) inputMap.get("id");
        if (inputId == null || inputId.trim().isEmpty()) {
            issues.add(new ValidationIssue(
                ValidationIssue.Severity.ERROR,
                dialogId,
                "input[" + index + "]",
                "Input is missing required 'id' field",
                "Add an 'id' field with a unique identifier"
            ));
            return issues; // Can't continue without ID
        }
        
        String type = (String) inputMap.get("type");
        if (type == null) {
            issues.add(new ValidationIssue(
                ValidationIssue.Severity.WARNING,
                dialogId,
                inputId,
                "Input type not specified, defaulting to 'text'",
                "Explicitly set 'type: text' or choose another input type"
            ));
        }
        
        // Validate text inputs specifically
        if ("text".equals(type) || type == null) {
            issues.addAll(validateTextInput(dialogId, inputId, inputMap));
        }
        
        // Validate number_range inputs
        if ("number_range".equals(type)) {
            issues.addAll(validateNumberRangeInput(dialogId, inputId, inputMap));
        }

        if ("boolean".equals(type)) {
            issues.addAll(validateBooleanInput(dialogId, inputId, inputMap));
        }

        return issues;
    }
    
    /**
     * Validates text input specific issues
     */
    private static List<ValidationIssue> validateTextInput(String dialogId, String inputId, Map<?, ?> inputMap) {
        List<ValidationIssue> issues = new ArrayList<>();
        
        Object initialObj = inputMap.get("initial");
        Object maxLengthObj = inputMap.get("max_length");
        
        if (initialObj instanceof String initial && maxLengthObj instanceof Number maxLengthNum) {
            int maxLength = maxLengthNum.intValue();
            
            if (usesFormatting(initial)) {
                String formatted = ColorManager.applyColorFormatting(initial);
                if (maxLength > 0 && formatted.length() > maxLength) {
                    issues.add(new ValidationIssue(
                        ValidationIssue.Severity.WARNING,
                        dialogId,
                        inputId,
                        String.format("Initial text with formatting (%d chars) exceeds max_length (%d)",
                            formatted.length(), maxLength),
                        String.format("Increase max_length to at least %d or simplify initial text",
                            formatted.length() + 50)
                    ));
                }
                
                if (maxLength > 0 && maxLength < 50) {
                    issues.add(new ValidationIssue(
                        ValidationIssue.Severity.INFO,
                        dialogId,
                        inputId,
                        "Using formatted initial text with low max_length may cause issues",
                        "Consider using max_length of 50+ when using ColorManager formatting"
                    ));
                }
            }
        }
        
        // Check for common max_length issues
        if (maxLengthObj instanceof Number maxLengthNum) {
            int maxLength = maxLengthNum.intValue();
            if (maxLength < 1) {
                issues.add(new ValidationIssue(
                    ValidationIssue.Severity.WARNING,
                    dialogId,
                    inputId,
                    "max_length is set to " + maxLength + " (effectively unlimited)",
                    "Set a reasonable max_length (e.g., 50-500) or remove the field"
                ));
            }
        }
        
        return issues;
    }
    
    /**
     * Validates number range input specific issues
     */
    private static List<ValidationIssue> validateNumberRangeInput(String dialogId, String inputId, Map<?, ?> inputMap) {
        List<ValidationIssue> issues = new ArrayList<>();
        
        Object minObj = inputMap.get("min");
        Object maxObj = inputMap.get("max");
        Object initialObj = inputMap.get("initial");
        
        if (minObj instanceof Number minNum && maxObj instanceof Number maxNum) {
            double min = minNum.doubleValue();
            double max = maxNum.doubleValue();
            
            if (min >= max) {
                issues.add(new ValidationIssue(
                    ValidationIssue.Severity.ERROR,
                    dialogId,
                    inputId,
                    String.format("min value (%.2f) must be less than max value (%.2f)", min, max),
                    "Ensure min < max for proper range functionality"
                ));
            }
            
            if (initialObj instanceof Number initialNum) {
                double initial = initialNum.doubleValue();
                if (initial < min || initial > max) {
                    issues.add(new ValidationIssue(
                        ValidationIssue.Severity.WARNING,
                        dialogId,
                        inputId,
                        String.format("initial value (%.2f) is outside range [%.2f, %.2f]", initial, min, max),
                        "Set initial value within the min-max range"
                    ));
                }
            }
        }
        
        return issues;
    }
    /**
     * Validates a boolean input configuration
     */
    private static List<ValidationIssue> validateBooleanInput(String dialogId, String inputId, Map<?, ?> inputMap) {
        List<ValidationIssue> issues = new ArrayList<>();

        // Boolean inputs have no required fields other than id, which is already checked

        // Optional values checks
        if (inputMap.containsKey("initial") && !(inputMap.get("initial") instanceof Boolean)) {
            issues.add(new ValidationIssue(
                    ValidationIssue.Severity.WARNING,
                    dialogId,
                    inputId,
                    "Boolean input 'initial' should be a boolean value",
                    "Change 'initial' to true or false"
            ));
        }

        return issues;
    }

    /**
     * Validates button configurations
     */
    private static List<ValidationIssue> validateButtons(String dialogId, ConfigurationSection buttonsSection) {
        List<ValidationIssue> issues = new ArrayList<>();
        
        boolean hasConfirm = buttonsSection.contains("confirm");
        boolean hasCancel = buttonsSection.contains("cancel");
        
        if (!hasConfirm && !hasCancel) {
            issues.add(new ValidationIssue(
                ValidationIssue.Severity.WARNING,
                dialogId,
                null,
                "No confirm or cancel buttons defined",
                "Add 'confirm' and/or 'cancel' button configurations"
            ));
        }
        
        return issues;
    }
    
    /**
     * Checks if text contains ColorManager formatting
     */
    private static boolean usesFormatting(String text) {
        return text.contains("<gradient:") || 
               text.contains("&") || 
               text.contains("#") ||
               text.contains("§");
    }
    
    /**
     * Formats validation issues for console output
     */
    public static void logValidationIssues(JavaPlugin plugin, List<ValidationIssue> issues) {
        if (issues.isEmpty()) {
            plugin.getLogger().info("✓ All dialog configurations are valid!");
            return;
        }
        
        plugin.getLogger().info("Dialog Configuration Validation Results:");
        plugin.getLogger().info("═══════════════════════════════════════");
        
        int warnings = 0, errors = 0, infos = 0;
        
        for (ValidationIssue issue : issues) {
            String prefix = switch (issue.getSeverity()) {
                case ERROR -> "❌ ERROR";
                case WARNING -> "⚠️  WARNING";
                case INFO -> "ℹ️  INFO";
            };
            
            String location = issue.getDialogId() + 
                (issue.getInputId() != null ? " -> " + issue.getInputId() : "");
            
            plugin.getLogger().warning(String.format("%s [%s]: %s", prefix, location, issue.getMessage()));
            if (issue.getSuggestion() != null) {
                plugin.getLogger().warning("   💡 " + issue.getSuggestion());
            }
            plugin.getLogger().warning("");
            
            switch (issue.getSeverity()) {
                case ERROR -> errors++;
                case WARNING -> warnings++;
                case INFO -> infos++;
            }
        }
        
        plugin.getLogger().info("Summary: " + errors + " errors, " + warnings + " warnings, " + infos + " info messages");
        plugin.getLogger().info("═══════════════════════════════════════");
    }
    
    /**
     * Formats validation issues for command sender output
     */
    public static void sendValidationResults(org.bukkit.command.CommandSender sender, List<ValidationIssue> issues) {
        if (issues.isEmpty()) {
            sender.sendMessage("§a✓ All dialog configurations are valid!");
            return;
        }
        
        sender.sendMessage("§6Dialog Configuration Validation Results:");
        sender.sendMessage("§6═══════════════════════════════════════");
        
        int warnings = 0, errors = 0, infos = 0;
        
        for (ValidationIssue issue : issues) {
            String color = switch (issue.getSeverity()) {
                case ERROR -> "§c";
                case WARNING -> "§e";
                case INFO -> "§b";
            };
            
            String icon = switch (issue.getSeverity()) {
                case ERROR -> "❌";
                case WARNING -> "⚠️";
                case INFO -> "ℹ️";
            };
            
            String location = issue.getDialogId() + 
                (issue.getInputId() != null ? " -> " + issue.getInputId() : "");
            
            sender.sendMessage(color + icon + " [" + location + "]: " + issue.getMessage());
            if (issue.getSuggestion() != null) {
                sender.sendMessage("§7   💡 " + issue.getSuggestion());
            }
            
            switch (issue.getSeverity()) {
                case ERROR -> errors++;
                case WARNING -> warnings++;
                case INFO -> infos++;
            }
        }
        
        sender.sendMessage("");
        sender.sendMessage("§6Summary: §c" + errors + " errors§6, §e" + warnings + " warnings§6, §b" + infos + " info messages");
        sender.sendMessage("§7Check console for more details.");
    }
}
