package com.soundify.server.account.domain.events;

import com.soundify.server.shared.domain.DomainEvent;
import com.soundify.server.shared.domain.Id;

import java.time.LocalDateTime;

public record AccountVerifedEvent(Id id, String username, String email,
                                  LocalDateTime verifiedAt) implements DomainEvent {
}
