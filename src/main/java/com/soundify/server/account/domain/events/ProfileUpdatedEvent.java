package com.soundify.server.account.domain.events;

import com.soundify.server.account.domain.models.Gender;
import com.soundify.server.shared.domain.DomainEvent;

import java.time.LocalDate;

public record ProfileUpdatedEvent(String string, String displayName, String avatar, LocalDate dateOfBirth,
                                  Gender gender) implements DomainEvent {
}
