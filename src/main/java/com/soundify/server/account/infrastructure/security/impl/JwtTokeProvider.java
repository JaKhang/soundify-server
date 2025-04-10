package com.soundify.server.account.infrastructure.security.impl;

import com.soundify.server.account.infrastructure.security.AccessToken;
import com.soundify.server.account.infrastructure.security.RefreshToken;
import com.soundify.server.account.infrastructure.security.TokenProvider;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtTokeProvider implements TokenProvider {
    @Override
    public Jwt generate(AccessToken context) {
        // TODO (PC, 10/04/2025): To change the body of an implemented method
        return null;
    }

    @Override
    public Jwt generate(RefreshToken context) {
        // TODO (PC, 10/04/2025): To change the body of an implemented method
        return null;
    }
}
