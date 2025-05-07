package com.soundify.server.shared.exceptions;

public class ResourceNotFoundException extends ApplicationException {
    private static final String TYPE = "NotFound";
    protected ResourceNotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, TYPE, 404, message);
    }
}
