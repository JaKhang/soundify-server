package com.soundify.server.account.application.dto;

import com.soundify.server.shared.domain.Id;

import java.time.LocalDateTime;


public record DeviceResponse(
        Id id,
        String ip,
        String os,
        String platform,
        boolean isCurrent,
        LocalDateTime loginAt
) {
}


