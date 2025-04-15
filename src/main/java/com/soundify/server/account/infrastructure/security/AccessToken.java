package com.soundify.server.account.infrastructure.security;

import com.soundify.server.shared.domain.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Data
@AllArgsConstructor
public class AccessToken {
    public static final String TYPE = "access";
    private Id jti;
    private Id sub;
    private Id rid;
    private int age;
    private Locale locale;
    private ChronoUnit unit;
    private LocalDate dob;
    private Collection<? extends GrantedAuthority> authorities;

}
