package com.soundify.server.account.infrastructure.security;

import com.soundify.server.account.application.exceptions.AuthenticationException;
import com.soundify.server.shared.exceptions.ErrorCode;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Locale;

@Log4j2
public class JwtPrincipalConvertor implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter authoritiesConverter;
    private final TokenProvider tokenProvider;
    public JwtPrincipalConvertor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
        this.authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");
        authoritiesConverter.setAuthoritiesClaimName("authorities");
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        try {
            UserPrincipal principal = tokenProvider.extractPrincipal(source);
            log.info("Convert jwt to principal {}", principal.id());
            return new UsernamePasswordAuthenticationToken(principal, source, principal.authorities());
        } catch (RuntimeException e) {
            throw new AuthenticationException(ErrorCode.FORBIDDEN);
        }
    }

    public static void main(String[] args) {
        System.out.println(Locale.of("en_US"));
    }
}
