package com.soundify.server.account.infrastructure.security;

import com.soundify.server.shared.domain.Id;
import lombok.Data;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

import java.time.temporal.ChronoUnit;
import java.util.Collection;

@Data
public class AccessToken {
    private Id jti;
    private Id sub;
    private Id rid;
    private int age;
    private ChronoUnit unit;
    private Collection<? extends GrantedAuthority> authorities;
}
