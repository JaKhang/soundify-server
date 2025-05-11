        package com.soundify.server.account.application.commands;

import com.soundify.server.shared.data.Image;
import com.soundify.server.account.domain.models.Gender;
import com.soundify.server.account.domain.models.Provider;
import com.soundify.server.account.domain.models.Role;
import com.soundify.server.account.domain.models.AccountStatus;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.mediator.MediatorRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public record CreateUserCommand(
        String email,
        String password,
        String displayName,
        List<Image> avatar,
        LocalDate dateOfBirth,
        Gender gender,
        Locale locale,
        Provider provider,
        LocalDateTime verifiedAt,
        Set<Role> roles,
        AccountStatus status
) implements MediatorRequest<Id> {
}
