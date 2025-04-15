package com.soundify.server.shared.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INVALID_REQUEST(1000, HttpStatus.BAD_REQUEST, "Invalid request"),
    UNAUTHORIZED(1001, HttpStatus.UNAUTHORIZED, "Unauthorized access"),
    FORBIDDEN(1002, HttpStatus.FORBIDDEN, "Forbidden action"),
    NOT_FOUND(1003, HttpStatus.NOT_FOUND, "Resource not found"),
    INTERNAL_SERVER_ERROR(1004, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    TOKEN_INVALID(1005, HttpStatus.UNAUTHORIZED, "Invalid or expired token"),
    BAD_CREDENTIALS(1005, HttpStatus.FORBIDDEN, "Invalid Credentials"),
    ACCOUNT_NOT_ACTIVE(1006, HttpStatus.FORBIDDEN, "Account not active");
        private final int code;
        private final HttpStatus status;
        private final String message;

        ErrorCode(int code, HttpStatus status, String message) {
            this.code = code;
            this.status = status;
            this.message = message;
        }

}