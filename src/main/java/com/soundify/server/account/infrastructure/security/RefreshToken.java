package com.soundify.server.account.infrastructure.security;

import com.soundify.server.shared.domain.Id;
import lombok.Data;

import java.time.temporal.ChronoUnit;

@Data
public class RefreshToken {
    private Id jid;
    private Id sub;
    private Id dev;
    private int age;
    private ChronoUnit unit;
}
