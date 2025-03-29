package com.soundify.server.account.domain.events;

import com.soundify.server.shared.domain.DomainEvent;
import com.soundify.server.shared.domain.Id;

public record AuthenticationTokenAddedEvent(Id id, String email, String value, int age) implements DomainEvent {
}
