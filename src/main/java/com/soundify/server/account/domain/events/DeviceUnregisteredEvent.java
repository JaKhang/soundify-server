package com.soundify.server.account.domain.events;

import com.soundify.server.shared.domain.DomainEvent;
import com.soundify.server.shared.domain.Id;

public record DeviceUnregisteredEvent(String string, Id id) implements DomainEvent {
}
