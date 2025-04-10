package com.soundify.server.account.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReauthenticateRequest(
        @NotBlank(message = "Refresh token must not be blank")
        String refreshToken
) {
}
