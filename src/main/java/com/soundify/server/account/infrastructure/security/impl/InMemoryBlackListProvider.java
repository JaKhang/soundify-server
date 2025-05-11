package com.soundify.server.account.infrastructure.security.impl;

import com.soundify.server.account.infrastructure.security.BackListProvider;
import com.soundify.server.shared.domain.Id;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
@Component
public class InMemoryBlackListProvider implements BackListProvider {
    private static final String BLACKLIST_PREFIX = "blacklist_";
    private final HashMap<String, String> redisTemplate;

    public InMemoryBlackListProvider() {
        this.redisTemplate = new HashMap<>();
    }

    @Override
    public boolean containsRefreshTokenId(Id rid) {
        return redisTemplate.containsKey(BLACKLIST_PREFIX + rid);
    }

    @Override
    public boolean containsDeviceId(Id id) {
        return redisTemplate.containsKey(BLACKLIST_PREFIX + id);
    }

    @Override
    public void addRefreshTokenId(Id id, int age, ChronoUnit unit) {
        Duration duration = Duration.of(age, unit);
        redisTemplate.put( BLACKLIST_PREFIX + id.toString(), "true");
    }

    @Override
    public void addDeviceId(Id id, int age, ChronoUnit unit) {
        Duration duration = Duration.of(age, unit);
        redisTemplate.put( BLACKLIST_PREFIX + id.toString(), "true");
    }
}
