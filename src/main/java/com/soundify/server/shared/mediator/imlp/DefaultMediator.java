package com.soundify.server.shared.mediator.imlp;

import com.soundify.server.shared.mediator.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DefaultMediator implements Mediator {

    ApplicationContext context;

    @Override
    @SuppressWarnings("unchecked")
    public void send(MediatorNotification mediatorNotification) {
        if (mediatorNotification == null) {
            throw new NullPointerException("The given request object cannot be null");
        }

        // Types used to find the exact handler that is required to handle the given request.
        // We are able to search all application beans by type from the Spring ApplicationContext.
        final Class<? extends MediatorNotification> requestType = mediatorNotification.getClass();

        // Retrieve RequestHandler beans based on request and response types.
        final String[] beanNames = context.getBeanNamesForType(ResolvableType.forClassWithGenerics(NotificationHandler.class, requestType));

        // There must be a registered handler.
        if (beanNames.length == 0) {
            throw new IllegalStateException("No handlers seemed to be registered to handle the given request. Make sure the handler is defined and marked as a spring component");
        }

        // There may not be more than one handler.
        if (beanNames.length > 1) {
            throw new IllegalStateException("More than one handlers found. Only one handler per request is allowed.");
        }

        final NotificationHandler<MediatorNotification> requestHandler = (NotificationHandler<MediatorNotification>) context.getBean(beanNames[0]);

        requestHandler.handle(mediatorNotification);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R send(MediatorRequest<R> mediatorRequest) {
        if (mediatorRequest == null) {
            throw new NullPointerException("The given request object cannot be null");
        }

        // Types used to find the exact handler that is required to handle the given request.
        // We are able to search all application beans by type from the Spring ApplicationContext.
        final ResolvableType requestType = ResolvableType.forClass(mediatorRequest.getClass()).as(MediatorRequest.class);
        final ResolvableType responseType = requestType.getGeneric(0);
        final ResolvableType handlerType = ResolvableType.forClassWithGenerics(RequestHandler.class, ResolvableType.forClass(mediatorRequest.getClass()), responseType);

        System.out.println(handlerType);
        // Retrieve RequestHandler beans based on request and response types.
        final String[] beanNames = context.getBeanNamesForType(handlerType);

        // There must be a registered handler.
        if (beanNames.length == 0) {
            throw new IllegalStateException("No handlers seemed to be registered to handle the given request. Make sure the handler is defined and marked as a spring component");
        }

        // There may not be more than one handler.
        if (beanNames.length > 1) {
            throw new IllegalStateException("More than one handlers found. Only one handler per request is allowed.");
        }

        final RequestHandler<MediatorRequest<R>, R> requestHandler = (RequestHandler<MediatorRequest<R>, R>) context.getBean(beanNames[0]);

        return requestHandler.handle(mediatorRequest);
    }




}
