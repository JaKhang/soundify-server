package com.soundify.server.shared.mediator;

public interface Mediator {
    void send(MediatorNotification mediatorNotification);

    <T>T send(MediatorRequest<T> mediatorRequest);

}
