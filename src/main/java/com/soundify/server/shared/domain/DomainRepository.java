package com.soundify.server.shared.domain;


public interface DomainRepository<T extends AggregateRoot, Id>{
    T findAggregate(Id id);
    void deleteAggregate(Id id);
    T saveAggregate(T t);
}
