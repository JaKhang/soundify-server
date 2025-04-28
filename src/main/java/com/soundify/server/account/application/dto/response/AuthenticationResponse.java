package com.soundify.server.account.application.dto.response;

import java.time.Duration;
import java.time.Instant;

public record AuthenticationResponse(
        TokenResponse accessToken,
        TokenResponse refreshToken,
        Duration age
) {
}
