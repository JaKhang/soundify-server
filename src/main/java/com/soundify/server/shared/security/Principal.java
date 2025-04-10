package com.soundify.server.shared.security;

import com.soundify.server.shared.domain.Id;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Locale;

public interface Principal {
    Id id();
    Collection<? extends GrantedAuthority> authorities();
    Locale locale();
}
