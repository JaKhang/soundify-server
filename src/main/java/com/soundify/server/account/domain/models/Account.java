package com.soundify.server.account.domain.models;

import com.soundify.server.shared.domain.AggregateRoot;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Account extends AggregateRoot {
    String email;
    String password;
    String displayName;
    String avatar;
    Provider provider;
    LocalDateTime verifiedAt;
    @Enumerated(EnumType.STRING)
    Set<Role> roles = new HashSet<>();
    Set<Device> devices = new HashSet<>();


}
