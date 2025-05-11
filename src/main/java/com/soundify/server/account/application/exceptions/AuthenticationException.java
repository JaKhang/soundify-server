package com.soundify.server.account.application.exceptions;

import com.soundify.server.shared.exceptions.ErrorCode;
import com.soundify.server.shared.exceptions.ApplicationException;

public class AuthenticationException extends ApplicationException {
    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthenticationException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
