package com.soundify.server.account.infrastructure.security.impl;

import com.soundify.server.account.infrastructure.security.BackListProvider;
import com.soundify.server.shared.domain.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Component
public class RedisBlackListProvider implements BackListProvider {
    private static final String BLACKLIST_PREFIX = "blacklist_";
    private final StringRedisTemplate redisTemplate;

    @Override
    public boolean containsRefreshTokenId(Id rid) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + rid));
    }

    @Override
    public boolean containsDeviceId(Id id) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + id));
    }

    @Override
    public void addRefreshTokenId(Id id, int age, ChronoUnit unit) {
        Duration duration = Duration.of(age, unit);
        redisTemplate.opsForValue().set( BLACKLIST_PREFIX + id.toString(), "true", duration);
    }

    @Override
    public void addDeviceId(Id id, int age, ChronoUnit unit) {
        Duration duration = Duration.of(age, unit);
        redisTemplate.opsForValue().set( BLACKLIST_PREFIX + id.toString(), "true", duration);
    }
}
