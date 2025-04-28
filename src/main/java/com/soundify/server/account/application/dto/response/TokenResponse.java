package com.soundify.server.account.application.dto.response;

public record TokenResponse(
        String token,
        String type
) {
}
