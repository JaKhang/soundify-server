package com.soundify.server.account.domain.events;

import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.DomainEvent;
import com.soundify.server.shared.domain.Id;

import java.util.List;

public record AvatarChangedEvents(Id id, List<Image> avatar) implements DomainEvent {
}
