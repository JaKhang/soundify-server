package com.soundify.server.account.http.request;

public record ResetPasswordRequest(
        String email,
        String token,
        String password
) {
}
