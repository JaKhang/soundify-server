package com.soundify.server.account.domain.models;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public record Token(String value, LocalDateTime createdAt, LocalDateTime expiredAt) {


    public boolean isValid() {
        return LocalDateTime.now().isBefore(expiredAt);
    }

    public boolean isExpired() {
        return !isValid();
    }

}
