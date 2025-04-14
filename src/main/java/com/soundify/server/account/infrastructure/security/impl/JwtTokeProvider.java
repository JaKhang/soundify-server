package com.soundify.server.account.infrastructure.security.impl;

import com.soundify.server.account.infrastructure.security.AccessToken;
import com.soundify.server.account.infrastructure.security.RefreshToken;
import com.soundify.server.account.infrastructure.security.TokenProvider;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;

public class JwtTokeProvider implements TokenProvider {
    JwtEncoder jwtEncoder;


    @Override
    public Jwt generate(AccessToken context) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(context.getJti().toString())
                .subject(context.getSub().toString())
                .claim("rid", context.getRid().toString())
                .claim("authorities", context.getAuthorities())
                .issuedAt(now)
                .expiresAt(now.plus(context.getAge(), context.getUnit()))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }

    @Override
    public Jwt generate(RefreshToken context) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(context.getJti().toString())
                .subject(context.getSub().toString())
                .claim("dev", context.getDev().toString())
                .issuedAt(now)
                .expiresAt(now.plus(context.getAge(), context.getUnit()))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }


}
