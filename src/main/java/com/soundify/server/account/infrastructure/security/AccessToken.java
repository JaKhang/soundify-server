package com.soundify.server.account.infrastructure.security;

import com.soundify.server.shared.domain.Id;
import lombok.Data;

import java.time.temporal.ChronoUnit;

@Data
public class AccessToken {
    private Id jti;
    private Id sub;
    private Id rid;
    private int age;
    private ChronoUnit unit;
}
