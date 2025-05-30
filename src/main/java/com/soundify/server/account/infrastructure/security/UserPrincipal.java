package com.soundify.server.account.infrastructure.security;

import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.security.Principal;
import org.springframework.security.core.GrantedAuthority;
import org.yaml.snakeyaml.events.Event;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Locale;


public record UserPrincipal (
         Id id,
         Id refreshTokenId,
         Collection<? extends GrantedAuthority> authorities,
         Locale locale,
         LocalDate dateOfBirth,
         Id deviceId
) implements Principal {

}
