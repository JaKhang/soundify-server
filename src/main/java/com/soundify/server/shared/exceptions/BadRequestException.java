package com.soundify.server.shared.exceptions;

public class BadRequestException extends ApplicationException {
    private static final String TYPE = "Bad Request";
    protected BadRequestException(String message) {
        super(ErrorCode.BAD_REQUEST, TYPE, 400, message);
    }
}
