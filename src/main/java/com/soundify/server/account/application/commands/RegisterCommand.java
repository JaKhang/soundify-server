package com.soundify.server.account.application.commands;

import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.mediator.MediatorRequest;

import java.time.LocalDate;
import java.util.Locale;

public record RegisterCommand(String email, String password, String displayName, LocalDate dateOfBirth, String gender, Locale locale, String provider) implements MediatorRequest<Id> {
}
