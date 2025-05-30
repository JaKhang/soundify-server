package com.soundify.server.account.application.commands;

import com.soundify.server.shared.mediator.MediatorNotification;

public record ResetPasswordRequestCommand(
        String email,
        String token,
        String newPassword

) implements MediatorNotification {
}
