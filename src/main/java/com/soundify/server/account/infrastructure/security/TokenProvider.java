package com.soundify.server.account.infrastructure.security;

import org.springframework.security.oauth2.jwt.Jwt;

public interface TokenProvider {

    Jwt generate(AccessToken context);

    Jwt generate(RefreshToken context);

    UserPrincipal extractPrincipal(Jwt jwt);

    RefreshPrincipal extractRefreshPrincipal(Jwt jwt);

}
