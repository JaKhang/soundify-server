package com.soundify.server.account.domain.models;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public record Token(String value, LocalDateTime createdAt, long age) {


    public boolean isValid() {
        return LocalDateTime.now().isBefore(createdAt.plusSeconds(age));
    }

    public boolean isExpired() {
        return !isValid();
    }

}
