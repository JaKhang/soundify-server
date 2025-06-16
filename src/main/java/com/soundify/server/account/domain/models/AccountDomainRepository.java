package com.soundify.server.account.domain.models;

import com.fasterxml.jackson.databind.introspect.AnnotationCollector;
import com.soundify.server.shared.domain.DomainRepository;
import com.soundify.server.shared.domain.Id;

public interface AccountDomainRepository extends DomainRepository<Account, Id> {
    Account findAggregateByUsernameOrEmail(String s);

    boolean existsByEmail(String email);
}
