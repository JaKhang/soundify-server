package com.soundify.server.account.application.commands;

import com.soundify.server.account.application.dto.AuthenticationResponse;
import com.soundify.server.shared.mediator.MediatorRequest;

public record AuthenticateCommand (
        String usernameOrEmail,
        String password,
        String os,
        String ip,
        String platform
) implements MediatorRequest<AuthenticationResponse>{
}
