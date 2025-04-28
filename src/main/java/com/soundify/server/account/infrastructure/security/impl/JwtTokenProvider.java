package com.soundify.server.account.infrastructure.security.impl;

import com.soundify.server.account.infrastructure.security.*;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.AuthenticationException;
import com.soundify.server.shared.exceptions.ErrorCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;
@Component
public class JwtTokenProvider implements TokenProvider {

    private final JwtEncoder jwtEncoder;
    private final JwtGrantedAuthoritiesConverter authoritiesConverter;

    public JwtTokenProvider(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");
        authoritiesConverter.setAuthoritiesClaimName(JwtClaimKey.AUTHORITIES.getValue());
    }

    @Override
    public Jwt generate(AccessToken context) {
        // Tạo payload cho AccessToken
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(context.getJti().toString()) // "jti"
                .subject(context.getSub().toString()) // "sub"
                .claim(JwtClaimKey.AUTHORITIES.getValue(), context.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())) // "authorities"
                .claim(JwtClaimKey.LOCALE.getValue(), context.getLocale().toString()) // "locale"
                .claim(JwtClaimKey.REFRESH_TOKEN.getValue(), context.getRid().toString())
                .claim(JwtClaimKey.DATE_OF_BIRTH.getValue(), context.getDob().toString())// "rid" (refresh token ID)
                .issuedAt(Instant.now()) // Thời gian phát hành
                .expiresAt(Instant.now().plus(context.getAge(), context.getUnit())) // Thời gian hết hạn
                .claim(JwtClaimKey.TOKEN_TYPE.getValue(), AccessToken.TYPE)
                .build();

        // Mã hóa JWT và trả về
        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }

    @Override
    public Jwt generate(RefreshToken context) {
        // Tạo payload cho RefreshToken
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(context.getJti().toString()) // "jti"
                .subject(context.getSub().toString()) // "sub"
                .claim(JwtClaimKey.DEVICE.getValue(), context.getDev().toString())
                .issuedAt(Instant.now()) // Thời gian phát hành
                .expiresAt(Instant.now().plus(context.getAge(), context.getUnit())) // Thời gian hết hạn
                .claim(JwtClaimKey.TOKEN_TYPE.getValue(), RefreshToken.TYPE)
                .build();

        // Mã hóa JWT và trả về
        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }

    @Override
    public UserPrincipal extractPrincipal(Jwt jwt) {
        if (!AccessToken.TYPE.equals(jwt.getClaimAsString(JwtClaimKey.TOKEN_TYPE.getValue())))
            throw new AuthenticationException(ErrorCode.FORBIDDEN);
        Id id = Id.from(jwt.getSubject()); // "sub"
        Id refreshTokenId = Id.from(jwt.getClaimAsString(JwtClaimKey.REFRESH_TOKEN.getValue())) ;
        Collection<? extends GrantedAuthority> authorities = authoritiesConverter.convert(jwt);
        Locale locale = Locale.forLanguageTag(jwt.getClaimAsString(JwtClaimKey.LOCALE.getValue()));
        LocalDate dateOfBirth = LocalDate.parse(jwt.getClaim(JwtClaimKey.DATE_OF_BIRTH.getValue()));

        return new UserPrincipal(id, refreshTokenId, authorities, locale, dateOfBirth);
    }

    @Override
    public RefreshPrincipal extractRefreshPrincipal(Jwt jwt) {
        if (RefreshToken.TYPE.equals(jwt.getClaimAsString(JwtClaimKey.TOKEN_TYPE.getValue())))
            throw new AuthenticationException(ErrorCode.FORBIDDEN);
        Id id = Id.from(jwt.getId()); // "jti" hoặc một claim khác
        Id accountId = Id.from(jwt.getSubject()); // "sub" hoặc một claim khác
        Id deviceId = Id.from(jwt.getClaimAsString(JwtClaimKey.DEVICE.getValue())); // "dev"
        return new RefreshPrincipal(id, accountId, deviceId);
    }
}
