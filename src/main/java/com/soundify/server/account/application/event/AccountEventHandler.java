package com.soundify.server.account.application.event;

import com.soundify.server.account.domain.events.DeviceUnregisteredEvent;
import com.soundify.server.account.infrastructure.security.BlackListProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModule;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;

@Slf4j
@Component
@FieldDefaults( level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AccountEventHandler {
    final BlackListProvider blackListProvider;
    @Value("${application.security.jwt.refresh-token-age}")
    private int refreshTokenAge;
    @Value("${application.security.jwt.refresh-token-age-unit}")
    private ChronoUnit refreshTokenUnit;
    @Transactional
    @EventListener
    @Async
    public void on(DeviceUnregisteredEvent event){
        log.info("On Device Unregistered Event");
        blackListProvider.addDeviceId(event.deviceId(), refreshTokenAge, refreshTokenUnit);
    }
}
