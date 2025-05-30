package com.soundify.server.account.application.commands.handler;

import com.soundify.server.account.application.commands.ResetPasswordRequestCommand;
import com.soundify.server.account.domain.models.Account;
import com.soundify.server.account.domain.models.AccountDomainRepository;
import com.soundify.server.shared.mediator.Handler;
import com.soundify.server.shared.mediator.NotificationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Handler
public class ResetPasswordCommandHandler implements NotificationHandler<ResetPasswordRequestCommand> {
    private final PasswordEncoder passwordEncoder;
    private final AccountDomainRepository accountRepository;
    @Override
    public void handle(ResetPasswordRequestCommand notification) {
        Account account = accountRepository.findAggregateByUsernameOrEmail(notification.email());
        account.resetPassword(notification.token(), passwordEncoder.encode(notification.newPassword()));
        accountRepository.saveAggregate(account);
    }
}
