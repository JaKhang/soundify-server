package com.soundify.server.account.infrastructure.security;

import com.soundify.server.shared.domain.Id;

import java.time.temporal.ChronoUnit;

public interface BackListProvider {
    boolean containsRefreshTokenId(Id rid);

    boolean containsDeviceId(Id id);

    boolean addRefreshTokenId(Id id, int age, ChronoUnit unit);

    boolean addDeviceId(Id id, int age, ChronoUnit unit);
}
