package com.soundify.server.account.application.commands;

import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.security.Principal;

public record LogoutCommand(Principal user) {
}
