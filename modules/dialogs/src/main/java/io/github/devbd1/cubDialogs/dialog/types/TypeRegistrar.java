package io.github.devbd1.cubDialogs.dialog.types;

import io.github.devbd1.cubDialogs.dialog.types.sub.ConfirmationDialogTypeHandler;
import io.github.devbd1.cubDialogs.dialog.types.sub.MultiActionDialogTypeHandler;
import io.github.devbd1.cubDialogs.dialog.types.sub.NoticeDialogTypeHandler;

import java.util.HashMap;
import java.util.Map;

public class TypeRegistrar {
    private static final Map<String, TypeInterface> handlers = new HashMap<>();

    static {
        registerHandler(new ConfirmationDialogTypeHandler());
        registerHandler(new MultiActionDialogTypeHandler());
        registerHandler(new NoticeDialogTypeHandler());
        // Removed: registerHandler(new DialogListDialogTypeHandler());
    }

    public static void registerHandler(TypeInterface handler) {
        handlers.put(handler.getTypeName().toLowerCase(), handler);
    }

    public static TypeInterface getHandler(String typeName) {
        return handlers.get(typeName.toLowerCase());
    }

    public static boolean isSupported(String typeName) {
        return handlers.containsKey(typeName.toLowerCase());
    }
}