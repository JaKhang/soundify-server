package com.soundify.server.shared.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public abstract class ApplicationException extends RuntimeException{
    private final int code;
    private final String type;
    private final int status;
    private final String message;

    protected ApplicationException(int code, String type, int status, String message) {
        this.code = code;
        this.type = type;
        this.status = status;
        this.message = message;
    }
}
