package com.soundify.server.account.infrastructure.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtResponseConverter implements Converter<Jwt, String> {

    @Override
    public String convert(Jwt source) {
        return source.getTokenValue();
    }
}
