package com.soundify.server.account.domain.events;

import com.soundify.server.shared.domain.DomainEvent;

public record PasswordChangedEvent(String string) implements DomainEvent {
}
