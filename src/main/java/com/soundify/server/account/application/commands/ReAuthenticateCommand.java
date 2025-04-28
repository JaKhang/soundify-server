package com.soundify.server.account.application.commands;

import com.soundify.server.account.application.dto.response.TokenResponse;
import com.soundify.server.shared.mediator.MediatorRequest;

public record ReAuthenticateCommand(
        String refreshToken,
        String ip,
        String os,
        String platform) implements MediatorRequest<TokenResponse>{

}
