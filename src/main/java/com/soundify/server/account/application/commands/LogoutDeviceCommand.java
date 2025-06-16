package com.soundify.server.account.application.commands;

import com.soundify.server.account.infrastructure.security.UserPrincipal;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.mediator.MediatorNotification;
import com.soundify.server.shared.security.Principal;

public record LogoutDeviceCommand(UserPrincipal principal, Id deviceId) implements MediatorNotification {
}
