package com.soundify.server.shared.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ErrorResponse(int code, int status, String message, @JsonInclude(JsonInclude.Include.NON_NULL) Object data) {

}
