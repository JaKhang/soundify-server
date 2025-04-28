package com.soundify.server.account.application.commands;

import co.elastic.clients.elasticsearch.security.AuthenticateResponse;
import com.soundify.server.account.application.dto.response.AuthenticationResponse;
import com.soundify.server.account.application.dto.response.TokenResponse;
import com.soundify.server.shared.mediator.MediatorRequest;

public record AuthenticateCommand (
        String usernameOrEmail,
        String password,
        String os,
        String ip,
        String platform
) implements MediatorRequest<AuthenticationResponse>{
}
