package com.soundify.server.account.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public record Token(String value, 
                    @Column(name = "token_created_at")
                    LocalDateTime createdAt,
                    @Column(name = "token_expired_at")
                    LocalDateTime expiredAt) {


    public boolean isValid() {
        return LocalDateTime.now().isBefore(expiredAt);
    }

    public boolean isExpired() {
        return !isValid();
    }

}
