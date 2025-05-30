package com.soundify.server.account.infrastructure.security.impl;

import com.soundify.server.account.infrastructure.security.TokenValueGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class RandomTokenValueGenerator implements TokenValueGenerator {

    @Value("${application.security.token-length}")
    private int length;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String generate() {
        if (length <= 0) {
            throw new IllegalArgumentException("Token length must be greater than 0");
        }

        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            token.append(CHARACTERS.charAt(index));
        }
        return token.toString();
    }
}
