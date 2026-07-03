package com.kukso.minecraft.dialogs.dialog;

import com.kukso.minecraft.dialogs.API.DialogActionContext;
import com.kukso.minecraft.dialogs.API.DialogKey;
import com.kukso.minecraft.dialogs.API.PayloadView;
import com.kukso.minecraft.dialogs.API.Registration;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleDialogActionRegistryTest {
    @Test
    void registrationHandleRemovesListenerFromFutureExecutions() {
        SimpleDialogActionRegistry registry = new SimpleDialogActionRegistry(Logger.getLogger("test"));
        DialogKey key = DialogKey.parse("kukso:test/action");
        AtomicInteger executions = new AtomicInteger();

        Registration registration = registry.register(key, context -> executions.incrementAndGet());

        assertTrue(registration.isActive());
        registry.executeListeners(new TestContext(key));
        assertEquals(1, executions.get());

        registration.unregister();

        assertFalse(registration.isActive());
        registry.executeListeners(new TestContext(key));
        assertEquals(1, executions.get());
    }

    @Test
    void unregisterAllDeactivatesEveryListenerForKey() {
        SimpleDialogActionRegistry registry = new SimpleDialogActionRegistry(Logger.getLogger("test"));
        DialogKey key = DialogKey.parse("kukso:test/action");
        AtomicInteger executions = new AtomicInteger();

        Registration first = registry.register(key, context -> executions.incrementAndGet());
        Registration second = registry.register(key, context -> executions.incrementAndGet());

        assertEquals(2, registry.unregisterAll(key));

        assertFalse(first.isActive());
        assertFalse(second.isActive());
        registry.executeListeners(new TestContext(key));
        assertEquals(0, executions.get());
    }

    private record TestContext(DialogKey key) implements DialogActionContext {
        @Override
        public PayloadView payload() {
            return new EmptyPayloadView();
        }

        @Override
        public UUID playerId() {
            return new UUID(0L, 1L);
        }

        @Override
        public Optional<String> playerName() {
            return Optional.of("Tester");
        }

        @Override
        public void reply(String message) {
        }
    }

    private static class EmptyPayloadView implements PayloadView {
        @Override
        public String getText(String key) {
            return null;
        }

        @Override
        public Integer getInt(String key) {
            return null;
        }

        @Override
        public Float getFloat(String key) {
            return null;
        }

        @Override
        public Boolean getBoolean(String key) {
            return null;
        }
    }
}
