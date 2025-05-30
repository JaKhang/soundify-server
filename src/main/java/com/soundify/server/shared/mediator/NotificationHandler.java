package com.soundify.server.shared.mediator;

public interface NotificationHandler<T extends MediatorNotification> {
    void handle(T notification);
}
