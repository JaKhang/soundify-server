package com.soundify.server.account.infrastructure.security;

public class IllegalJwtException extends RuntimeException {
    public IllegalJwtException(String message) {
        super(message);
    }
}
