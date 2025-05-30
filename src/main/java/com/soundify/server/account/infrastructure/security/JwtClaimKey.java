package com.soundify.server.account.infrastructure.security;

import lombok.Getter;

@Getter
public enum JwtClaimKey {
    JTI("jti"),
    SUBJECT("sub"),
    REFRESH_TOKEN("rid"),
    AUTHORITIES("authorities"),
    DEVICE("dev"),
    LOCALE("locale"),
    DATE_OF_BIRTH("dob"),
    TOKEN_TYPE("type");

    private final String value;

    JwtClaimKey(String value) {
        this.value = value;
    }

}
