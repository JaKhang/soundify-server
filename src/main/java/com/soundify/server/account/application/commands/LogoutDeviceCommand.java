package com.soundify.server.account.application.commands;

import com.soundify.server.shared.domain.Id;

public record LogoutDeviceCommand(Id accountId, Id deviceId) {
}
