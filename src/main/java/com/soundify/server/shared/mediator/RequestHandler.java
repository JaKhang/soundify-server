package com.soundify.server.shared.mediator;

public interface RequestHandler <T extends MediatorRequest<R>,R>{
    R handle(T request);
}
