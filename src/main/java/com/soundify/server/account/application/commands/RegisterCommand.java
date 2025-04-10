package com.soundify.server.account.application.commands;

public record RegisterCommand(String email, String password, String displayName, String dateOfBirth, String gender, String locale, String provider) {
}
