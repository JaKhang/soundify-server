package com.soundify.server.account.application.dto;

import java.time.Duration;

public record AuthenticationResponse(
        TokenResponse accessToken,
        TokenResponse refreshToken,
        Duration age
) {
}
