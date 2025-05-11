package com.soundify.server.shared.exceptions;

public class ResourceNotFoundException extends ApplicationException {
    public ResourceNotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND);
    }

    protected ResourceNotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }
}
