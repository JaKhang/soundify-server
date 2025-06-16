package com.soundify.server.account.infrastructure.security;

import com.soundify.server.shared.domain.Id;

public record RefreshPrincipal(
        Id id,
        Id accountId,
        Id deviceId
) {
}
