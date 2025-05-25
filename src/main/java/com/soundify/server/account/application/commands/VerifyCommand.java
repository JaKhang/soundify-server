package com.soundify.server.account.application.commands;

import com.soundify.server.shared.mediator.MediatorNotification;

public record VerifyCommand(
        String token,
        String email
) implements MediatorNotification {
}
