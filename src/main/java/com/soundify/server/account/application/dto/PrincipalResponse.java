package com.soundify.server.account.application.dto;

import com.soundify.server.account.domain.models.AccountStatus;
import com.soundify.server.shared.data.Image;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public record PrincipalResponse(
        String name,
        String username,
        List<Image> avatar,
        LocalDate dob,
        Locale locale,
        String email,
        boolean isVerified,
        AccountStatus status
) {
}
