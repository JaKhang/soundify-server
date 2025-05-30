package com.soundify.server.shared.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ErrorResponse(
        int code,
        String message,
        String type,
        @JsonInclude(JsonInclude.Include.NON_NULL) Object data) {

}
