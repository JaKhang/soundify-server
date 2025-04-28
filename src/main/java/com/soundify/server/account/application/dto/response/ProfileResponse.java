package com.soundify.server.account.application.dto.response;

import com.soundify.server.shared.data.Image;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public record ProfileResponse(
        String name,
        String username,
        List<Image> avatar,
        LocalDate dob,
        Locale locale,
        String email,
        boolean isVerified
) {
}
