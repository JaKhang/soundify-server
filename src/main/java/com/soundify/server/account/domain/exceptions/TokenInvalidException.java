package com.soundify.server.account.domain.exceptions;

import com.soundify.server.shared.exceptions.DomainException;

public class TokenInvalidException extends DomainException {
    public TokenInvalidException(String message) {
        super(message);
    }
}
