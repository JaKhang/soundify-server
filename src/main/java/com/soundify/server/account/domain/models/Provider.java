package com.soundify.server.account.domain.models;

import lombok.Getter;

@Getter
public enum Provider {
    GOOGLE("google"),
    FACEBOOK("facebook"),
    TWITTER("twitter"),
    APPLE("apple"),
    GITHUB("github"),
    MICROSOFT("microsoft"),
    LINKEDIN("linkedin"),
    SOUNDIFY("soundify");


    public static final Provider LOCAL_PROVIDER= SOUNDIFY;

    private final String value;

    Provider(String value) {
        this.value = value;
    }

    public static Provider fromValue(String value) {
        for (Provider provider : Provider.values()) {
            if (provider.value.equalsIgnoreCase(value)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("Unknown provider: " + value);
    }
}
