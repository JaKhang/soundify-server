package com.soundify.server.account.infrastructure.security;

import org.springframework.security.oauth2.jwt.Jwt;

import java.security.Principal;

public interface TokenProvider {

    Jwt generate(AccessToken context);

    Jwt generate(RefreshToken context);

    Principal


}
