package com.soundify.server.account.domain.events;

import com.soundify.server.shared.domain.DomainEvent;

import java.time.LocalDateTime;

public record DeviceRegisteredEvent(String string, String os, String ip, String platform,
                                    LocalDateTime loginAt) implements DomainEvent {
}
