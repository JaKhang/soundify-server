package com.soundify.server.account.domain.models;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public enum Role implements GrantedAuthority {
    USER("Standard User", Permission.READ, Permission.MANAGE_PLAYLISTS),
    PREMIUM_USER("Premium User", Permission.READ, Permission.WRITE, Permission.MANAGE_PLAYLISTS),
    ARTIST("Artist", Permission.READ, Permission.WRITE, Permission.MANAGE_MUSIC, Permission.VIEW_REPORTS),
    DISTRIBUTOR("Distributor", Permission.READ, Permission.MANAGE_MUSIC, Permission.APPROVE_RELEASES, Permission.MANAGE_COPYRIGHT, Permission.MANAGE_REVENUE),
    ADMIN("Administrator", Permission.READ, Permission.WRITE, Permission.EXECUTE, Permission.DELETE, Permission.MANAGE_USERS, Permission.MANAGE_PRODUCTS, Permission.MANAGE_ORDERS, Permission.VIEW_REPORTS, Permission.CONFIGURE_SETTINGS, Permission.APPROVE_TRANSACTIONS, Permission.MANAGE_INVENTORY, Permission.MANAGE_MUSIC, Permission.MANAGE_COPYRIGHT, Permission.APPROVE_RELEASES, Permission.MANAGE_REVENUE);

    private final String description;
    private final List<Permission> permissions;

    Role(String description, Permission... permissions) {
        this.description = description;
        this.permissions = Arrays.asList(permissions);
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }

    public Collection<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> authorities = permissions.stream()
                .map(permission -> (GrantedAuthority) permission::name)
                .collect(Collectors.toList());
        authorities.add(this);
        return authorities;

    }
}