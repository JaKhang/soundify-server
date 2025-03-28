package com.soundify.server.shared.exceptions;

public class NotFoundException extends SystemException {
    public NotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND);
    }
}
