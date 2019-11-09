package com.framework.core.notifications.interfaces;

import com.framework.core.notifications.commandes.ContactEvent;

public interface ContactEventSubscriber {
    void updateEvent(ContactEvent collision);
}
