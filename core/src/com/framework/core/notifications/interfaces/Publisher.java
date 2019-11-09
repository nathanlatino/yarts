package com.framework.core.notifications.interfaces;

public interface Publisher<T> {
    void subscribe(T subscriber);
    void unSubscribe(T subscriber);
    void notifySubscribers();
}
