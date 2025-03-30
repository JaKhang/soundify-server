package com.soundify.server.account.infrastructure.security;

import com.soundify.server.security.UserPrincipal;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.AuthenticationException;
import com.soundify.server.shared.exceptions.ErrorCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Locale;

@Log4j2
public class JwtPrincipalConvertor implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter authoritiesConverter;

    public JwtPrincipalConvertor() {
        this.authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");
        authoritiesConverter.setAuthoritiesClaimName("authorities");
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        if (!"access".equals(source.getClaimAsString("type")))
            throw new AuthenticationException(ErrorCode.FORBIDDEN);

        try {
            Id id = Id.from(source.getSubject());
            Id rid = Id.from(source.getClaimAsString("rid"));
            Locale locale = Locale.forLanguageTag(source.getClaimAsString("locales"));
            Collection<? extends GrantedAuthority> authorities = authoritiesConverter.convert(source);
            UserPrincipal jwtPrincipal = new UserPrincipal(id, rid, authorities, locale);
            return new UsernamePasswordAuthenticationToken(jwtPrincipal, source, authorities);
        } catch (RuntimeException e) {
            throw new AuthenticationException(ErrorCode.FORBIDDEN);
        }
    }

    public static void main(String[] args) {
        System.out.println(Locale.of("en_US"));
    }
}
