package com.soundify.server.shared.exceptions;

public class ResourceNotFoundException extends SystemException {
    protected ResourceNotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }
}
