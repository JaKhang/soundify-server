package com.soundify.server.account.http.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LocalAuthenticateRequest(
        @NotBlank(message = "Credentials must not be blank")
        @Pattern(
                regexp = "(^[a-zA-Z0-9_]{8,16}$)|(^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$)",
                message = "Must be a valid username (8-16 chars, letters/numbers/_) or a valid email"
        )
        String usernameOrEmail,
        @NotBlank(message = "Credentials must not be blank")
        String password
) {
}
