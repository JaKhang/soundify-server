package com.soundify.server.shared.domain;


import jakarta.persistence.Transient;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public abstract class AggregateRoot extends AbstractEntity {
    @Transient
    private final Collection<DomainEvent> domainEvents ;

    public AggregateRoot(Id id) {
        super(id);
        this.domainEvents = new HashSet<>();
    }

    protected AggregateRoot(){
        this.domainEvents = new HashSet<>();
    }



    @DomainEvents
    public Collection<DomainEvent> events() {
        return Collections.unmodifiableCollection(domainEvents);
    }

    @AfterDomainEventPublication
    public void onPublishedEvents(){
        domainEvents.clear();
    }



    public void registerEvents(DomainEvent domainEvent){
        domainEvents.add(domainEvent);
    }

}
