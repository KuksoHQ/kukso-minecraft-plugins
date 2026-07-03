package com.kukso.minecraft.dialogs.dialog;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kukso.minecraft.dialogs.dialog.DialogConfigValidator.ValidationIssue.Severity.ERROR;
import static com.kukso.minecraft.dialogs.dialog.DialogConfigValidator.ValidationIssue.Severity.WARNING;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DialogConfigValidatorTest {
    @Test
    void validateDialogReportsInputAndButtonConfigurationProblems() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("title", "Experience Config");
        config.set("inputs", List.of(
                numberRangeInput(),
                invalidBooleanInput(),
                inputWithoutId()
        ));
        config.createSection("buttons");

        List<DialogConfigValidator.ValidationIssue> issues =
                DialogConfigValidator.validateDialog("exp_config", config);

        assertHasIssue(issues, ERROR, "difficulty", "min value (10.00) must be less than max value (5.00)");
        assertHasIssue(issues, WARNING, "difficulty", "initial value (20.00) is outside range");
        assertHasIssue(issues, WARNING, "enabled", "Boolean input 'initial' should be a boolean value");
        assertHasIssue(issues, ERROR, "input[2]", "Input is missing required 'id' field");
        assertHasIssue(issues, WARNING, null, "No confirm or cancel buttons defined");
    }

    @Test
    void validateDialogAcceptsBasicValidConfiguration() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("title", "Server Rules");
        config.set("inputs", List.of(Map.of(
                "id", "comment",
                "type", "text",
                "max_length", 120
        )));
        config.createSection("buttons.confirm");

        List<DialogConfigValidator.ValidationIssue> issues =
                DialogConfigValidator.validateDialog("server_rules", config);

        assertTrue(issues.isEmpty());
    }

    private static Map<String, Object> numberRangeInput() {
        Map<String, Object> input = new HashMap<>();
        input.put("id", "difficulty");
        input.put("type", "number_range");
        input.put("min", 10);
        input.put("max", 5);
        input.put("initial", 20);
        return input;
    }

    private static Map<String, Object> invalidBooleanInput() {
        Map<String, Object> input = new HashMap<>();
        input.put("id", "enabled");
        input.put("type", "boolean");
        input.put("initial", "yes");
        return input;
    }

    private static Map<String, Object> inputWithoutId() {
        Map<String, Object> input = new HashMap<>();
        input.put("type", "text");
        return input;
    }

    private static void assertHasIssue(List<DialogConfigValidator.ValidationIssue> issues,
                                       DialogConfigValidator.ValidationIssue.Severity severity,
                                       String inputId,
                                       String messagePart) {
        assertTrue(
                issues.stream().anyMatch(issue ->
                        issue.getSeverity() == severity
                                && ((inputId == null && issue.getInputId() == null)
                                        || (inputId != null && inputId.equals(issue.getInputId())))
                                && issue.getMessage().contains(messagePart)
                ),
                "Expected " + severity + " for " + inputId + " containing: " + messagePart
        );
    }
}
