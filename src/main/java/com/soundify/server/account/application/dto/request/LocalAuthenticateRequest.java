package com.soundify.server.account.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LocalAuthenticateRequest(
        @NotBlank(message = "Credentials must not be blank")
        String usernameOrEmail,
        @NotBlank(message = "Credentials must not be blank")
        String password
) {
}
