package com.soundify.server.account.application.commands;

import java.time.LocalDate;

public record UpdateProfileCommand(
        String displayName,
        LocalDate dateOfBirth,
        String gender
) {
}
