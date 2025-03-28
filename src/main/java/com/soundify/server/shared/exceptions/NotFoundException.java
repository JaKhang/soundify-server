package com.soundify.server.shared.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends SystemException {

    public NotFoundException(String message) {
        super(message,ErrorCode.NOT_FOUND);
    }
}
