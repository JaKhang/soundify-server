package com.soundify.server.metadata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ApiResponse<T>(int status, String message, @JsonInclude(JsonInclude.Include.NON_NULL) T data) {
}
