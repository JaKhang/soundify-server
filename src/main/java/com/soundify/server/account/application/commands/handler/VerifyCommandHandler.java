package com.soundify.server.account.application.commands.handler;

import com.soundify.server.account.application.commands.VerifyCommand;
import com.soundify.server.account.domain.models.Account;
import com.soundify.server.account.domain.models.AccountDomainRepository;
import com.soundify.server.shared.mediator.Handler;
import com.soundify.server.shared.mediator.NotificationHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;

@Handler
@RequiredArgsConstructor
@Log4j2
public class VerifyCommandHandler implements NotificationHandler<VerifyCommand> {
    private final AccountDomainRepository accountRepository;

    @Override
    public void handle(VerifyCommand notification) {
        Assert.notNull(notification.email(), "email cannot be null");
        Assert.notNull(notification.token(), "token cannot be null");
        log.info("Verifying account with email: {}", notification.email());
        Account account = accountRepository.findAggregateByUsernameOrEmail(notification.email());
        account.verifyEmail(notification.token());
        accountRepository.saveAggregate(account);
        log.info("Account with email: {} verified", notification.email());

    }
}
