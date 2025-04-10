package com.soundify.server.account.application.commands;

import com.soundify.server.shared.domain.Id;

public record LogoutCommand(Id accountId, Id refreshTokenId, Id deviceId) {
}
