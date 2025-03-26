package com.soundify.server.account.domain.models;

import com.soundify.server.shared.domain.DomainEvent;

public record PasswordChangedEvent(String string) implements DomainEvent {
}
