package com.soundify.server.account.application.commands.handler;

import com.soundify.server.account.application.commands.ChangePasswordCommand;
import com.soundify.server.account.domain.models.Account;
import com.soundify.server.account.domain.models.AccountDomainRepository;
import com.soundify.server.shared.mediator.Handler;
import com.soundify.server.shared.mediator.NotificationHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

@Handler
@RequiredArgsConstructor
@Log4j2
public class ChangePasswordCommandHandler implements NotificationHandler<ChangePasswordCommand> {
    private final AccountDomainRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void handle(ChangePasswordCommand notification) {
        Assert.notNull(notification.principal(), "principal cannot be null");
        Assert.notNull(notification.oldPassword(), "oldPassword cannot be null");
        Assert.notNull(notification.newPassword(), "newPassword cannot be null");
        log.info("Handling change password command for account with id: {}", notification.principal().id());
        Account account = accountRepository.findAggregate(notification.principal().id());
        account.changePassword(notification.oldPassword(), notification.newPassword(), passwordEncoder);
        accountRepository.saveAggregate(account);
        log.info("Password changed for account with id: {}", notification.principal().id());
    }
}
