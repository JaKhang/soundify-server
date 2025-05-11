package com.soundify.server.shared.exceptions;

import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException{
    private final ErrorCode errorCode;

    protected ApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ApplicationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
