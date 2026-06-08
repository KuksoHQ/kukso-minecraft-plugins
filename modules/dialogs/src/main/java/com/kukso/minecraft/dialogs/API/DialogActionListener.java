package com.kukso.minecraft.dialogs.API;

/**
 * Functional listener for handling a dialog custom action.
 */
@FunctionalInterface
public interface DialogActionListener {

    /**
     * Invoked when a dialog action with a matching key is triggered.
     *
     * @param context action context including key, payload, and reply helper
     */
    void onAction(DialogActionContext context);
}
