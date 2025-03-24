package com.soundify.server.account.domain.models;

import com.soundify.server.shared.domain.AggregateRoot;

import java.util.HashSet;
import java.util.Set;

public class Account extends AggregateRoot {

    String email;
    String password;
    String displayName;
    String avatarUrl;
    Set<Role> roles = new HashSet<>();
    Set<Device> devices = new HashSet<>();
    Provider provider;

}
