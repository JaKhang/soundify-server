package com.soundify.server.account.application.commands;

import com.soundify.server.shared.mediator.MediatorNotification;

public record RequestResetPasswordTokenCommand(
        String email
) implements MediatorNotification {
}
