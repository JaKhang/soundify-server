package com.soundify.server.shared.exceptions;

public class BadRequestException extends SystemException {
    public BadRequestException(String message) {
        super(message, ErrorCode.INVALID_REQUEST);
    }
}
