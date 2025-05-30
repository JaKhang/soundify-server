package com.soundify.server.account.application.commands.handler;

import com.soundify.server.account.application.commands.LogoutDeviceCommand;
import com.soundify.server.account.domain.models.Account;
import com.soundify.server.account.domain.models.AccountDomainRepository;
import com.soundify.server.account.infrastructure.security.BlackListProvider;
import com.soundify.server.shared.mediator.Handler;
import com.soundify.server.shared.mediator.NotificationHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.temporal.ChronoUnit;

@Slf4j
@Handler
@RequiredArgsConstructor
public class LogoutDeviceCommandHandler implements NotificationHandler<LogoutDeviceCommand> {
    private final AccountDomainRepository accountRepository;
    private final BlackListProvider blackListProvider;
    @Value("${application.security.jwt.refresh-token-age}")
    private int refreshTokenAge;
    @Value("${application.security.jwt.refresh-token-age-unit}")
    private ChronoUnit refreshTokenUnit;

    @Override
    public void handle(LogoutDeviceCommand notification) {
        Account account = accountRepository.findAggregate(notification.principal().id());
        account.unregisterDevice(notification.deviceId());
        accountRepository.saveAggregate(account);
        blackListProvider.addDeviceId(notification.deviceId(), refreshTokenAge, refreshTokenUnit);
    }
}
