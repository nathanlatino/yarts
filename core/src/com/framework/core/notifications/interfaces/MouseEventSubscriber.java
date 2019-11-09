package com.framework.core.notifications.interfaces;

import com.framework.core.notifications.commandes.MouseEvent;

public interface MouseEventSubscriber {
    void updateEvent(MouseEvent mouseEvent);
}
