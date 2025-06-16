package com.soundify.server.account.infrastructure.security;

import com.soundify.server.shared.domain.Id;

import java.time.temporal.ChronoUnit;

public interface BlackListProvider {
    boolean containsRefreshTokenId(Id rid);

    boolean containsDeviceId(Id id);

    void addRefreshTokenId(Id id, int age, ChronoUnit unit);

    void addDeviceId(Id id, int age, ChronoUnit unit);
}
