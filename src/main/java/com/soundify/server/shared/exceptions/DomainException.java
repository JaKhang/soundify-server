package com.soundify.server.shared.exceptions;

public class DomainException extends SystemException {
    public DomainException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    protected DomainException(ErrorCode errorCode) {
        super(errorCode);
    }
}
