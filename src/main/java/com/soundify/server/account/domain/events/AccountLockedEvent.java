package com.soundify.server.account.domain.events;

import com.soundify.server.shared.domain.DomainEvent;
import com.soundify.server.shared.domain.Id;

public record AccountLockedEvent(Id id) implements DomainEvent {
}
