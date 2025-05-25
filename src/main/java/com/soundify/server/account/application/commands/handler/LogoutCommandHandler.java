package com.soundify.server.account.application.commands.handler;

import com.soundify.server.account.application.commands.LogoutCommand;
import com.soundify.server.account.domain.models.Account;
import com.soundify.server.account.domain.models.AccountDomainRepository;
import com.soundify.server.account.infrastructure.security.BlackListProvider;
import com.soundify.server.shared.mediator.Handler;
import com.soundify.server.shared.mediator.NotificationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Handler
public class LogoutCommandHandler implements NotificationHandler<LogoutCommand> {
    private final PasswordEncoder passwordEncoder;
    private final AccountDomainRepository accountRepository;
    private final BlackListProvider blackListProvider;
    @Value("${application.security.jwt.refresh-token-age}")
    private int refreshTokenAge;
    @Value("${application.security.jwt.refresh-token-age-unit}")
    private ChronoUnit refreshTokenUnit;

    @Override
    public void handle(LogoutCommand notification) {
        Account account = accountRepository.findAggregate(notification.user().id());
        account.unregisterDevice(notification.user().deviceId());
        accountRepository.saveAggregate(account);
        blackListProvider.addRefreshTokenId(notification.user().refreshTokenId(), refreshTokenAge, refreshTokenUnit);
    }
}
