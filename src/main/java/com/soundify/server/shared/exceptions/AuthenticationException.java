package com.soundify.server.shared.exceptions;

public class AuthenticationException extends SystemException {
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthenticationException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
