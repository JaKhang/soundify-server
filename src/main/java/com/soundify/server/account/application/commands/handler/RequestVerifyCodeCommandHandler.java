package com.soundify.server.account.application.commands.handler;

import com.soundify.server.account.application.commands.RequestVerifyCodeCommand;
import com.soundify.server.account.domain.models.Account;
import com.soundify.server.account.domain.models.AccountDomainRepository;
import com.soundify.server.account.infrastructure.security.TokenValueGenerator;
import com.soundify.server.shared.mediator.Handler;
import com.soundify.server.shared.mediator.NotificationHandler;
import com.soundify.server.shared.mediator.RequestHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.temporal.ChronoUnit;

@Slf4j
@Handler
@RequiredArgsConstructor
public class RequestVerifyCodeCommandHandler implements NotificationHandler<RequestVerifyCodeCommand> {
    private final AccountDomainRepository accountRepository;
    private final TokenValueGenerator tokenValueGenerator;
    @Value("${application.security.verify-token-age}")
    private int verifyTokenAge;
    @Value("${application.security.verify-token-age-unit}")
    private ChronoUnit verifyTokenAgeUnit;
    @Override
    public void handle(RequestVerifyCodeCommand notification) {
        log.info("RequestVerifyCodeCommandHandler");
        Account account = accountRepository.findAggregateByUsernameOrEmail(notification.email());
        String token = tokenValueGenerator.generate();
        account.addVerificationToken(token, verifyTokenAge, verifyTokenAgeUnit);
        accountRepository.saveAggregate(account);
        log.info("Account:{} request verify", notification.email());
    }
}
