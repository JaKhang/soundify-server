package com.soundify.server.account.application.commands.handler;

import com.soundify.server.account.application.commands.RequestResetPasswordTokenCommand;
import com.soundify.server.account.domain.models.Account;
import com.soundify.server.account.domain.models.AccountDomainRepository;
import com.soundify.server.account.infrastructure.security.TokenValueGenerator;
import com.soundify.server.shared.mediator.Handler;
import com.soundify.server.shared.mediator.NotificationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.temporal.ChronoUnit;

@Handler
@RequiredArgsConstructor
public class RequestResetPasswordTokenCommandHandler implements NotificationHandler<RequestResetPasswordTokenCommand> {
    private final AccountDomainRepository accountDomainRepository;
    private final TokenValueGenerator generator;
    @Value("${application.security.verify-token-age}")
    private int resetTokenAge;
    @Value("${application.security.verify-token-age-unit}")
    private ChronoUnit resetTokenAgeUnit;
    @Override
    public void handle(RequestResetPasswordTokenCommand notification) {

        Account account = accountDomainRepository.findAggregateByUsernameOrEmail(notification.email());
        String value = generator.generate();
        account.addVerificationToken(value, resetTokenAge, resetTokenAgeUnit);
        accountDomainRepository.saveAggregate(account);

    }
}
