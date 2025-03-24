package com.soundify.server.shared.exceptions;

public record ErrorResponse(int code, int status, String message) {

}
