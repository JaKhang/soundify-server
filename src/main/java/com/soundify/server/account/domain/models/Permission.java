package com.soundify.server.account.domain.models;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
    READ,
    WRITE,
    EXECUTE,
    DELETE,
    MANAGE_USERS,
    MANAGE_ORDERS,
    MANAGE_PRODUCTS,
    VIEW_REPORTS,
    CONFIGURE_SETTINGS,
    APPROVE_TRANSACTIONS,
    MANAGE_INVENTORY,
    MANAGE_PLAYLISTS,
    MANAGE_MUSIC,
    MANAGE_COPYRIGHT,
    APPROVE_RELEASES,
    MANAGE_REVENUE;

    @Override
    public String getAuthority() {
        return "PERMISSION_" + name();
    }
}
