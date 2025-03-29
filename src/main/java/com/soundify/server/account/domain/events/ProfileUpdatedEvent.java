package com.soundify.server.account.domain.events;

import com.soundify.server.account.domain.models.Gender;
import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.DomainEvent;

import java.time.LocalDate;
import java.util.List;

public record ProfileUpdatedEvent(String string, String displayName, List<Image> avatar, LocalDate dateOfBirth,
                                  Gender gender) implements DomainEvent {
}
