package com.soundify.server.account.infrastructure.security;

import lombok.Getter;

@Getter
public enum JwtClaimKey {
    JTI("jti"),
    SUBJECT("sub"),
    REFRESH_TOKEN("rid"),
    AUTHORITIES("authorities"),
    DEVICE("dev");

    private final String value;

    JwtClaimKey(String value) {
        this.value = value;
    }

}
