package com.soundify.server.security;

import com.soundify.server.shared.domain.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Locale;

@AllArgsConstructor
@Getter
public class UserPrincipal {
    private Id id;
    private Id refreshTokenId;
    private Collection<? extends GrantedAuthority> authorities;
    private Locale locale;
}
