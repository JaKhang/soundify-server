package com.soundify.server.account.application.commands;

import org.springframework.web.multipart.MultipartFile;

import com.soundify.server.shared.domain.Id;

public record ChangeAvatarCommand(Id accountId, MultipartFile[] avatars) {
}
