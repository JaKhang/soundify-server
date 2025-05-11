package com.soundify.server.shared.exceptions;

public class BadRequestException extends ApplicationException {
    protected BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
    public BadRequestException(String message) {
        super(message, ErrorCode.INVALID_REQUEST);
    }
}
