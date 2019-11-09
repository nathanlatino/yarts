package com.framework.core.notifications.interfaces;

import com.framework.core.notifications.commandes.KeyboardEvent;

public interface KeyboardEventSubscriber {
    void updateEvent(KeyboardEvent keyboardEvent);
}
