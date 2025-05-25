package com.soundify.server.account.application.commands;

import com.soundify.server.shared.mediator.MediatorNotification;
import com.soundify.server.shared.security.Principal;

public record ChangePasswordCommand(
        Principal principal,
        String oldPassword,
        String newPassword
) implements MediatorNotification {
}
