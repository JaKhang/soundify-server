package com.soundify.server.shared.exceptions;

import lombok.Getter;

@Getter
public abstract class SystemException extends RuntimeException{
    private final ErrorCode errorCode;

    protected SystemException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public SystemException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
