package com.soundify.server.account.http.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyRequest(
        @NotBlank
        String token,
        @Email
        String email
) {
}
