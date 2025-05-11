package com.soundify.server.account.application.commands.handler;

import com.soundify.server.account.application.commands.CreateUserCommand;
import com.soundify.server.account.domain.models.Account;
import com.soundify.server.account.domain.models.AccountDomainRepository;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.BadRequestException;
import com.soundify.server.shared.exceptions.DomainException;
import com.soundify.server.shared.exceptions.ErrorCode;
import com.soundify.server.shared.mediator.Handler;
import com.soundify.server.shared.mediator.RequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@RequiredArgsConstructor
@Handler
public class CreateUserCommandHandler implements RequestHandler<CreateUserCommand, Id> {
    private final PasswordEncoder passwordEncoder;
    private final AccountDomainRepository accountRepository;




    @Override
    public Id handle(CreateUserCommand command) {
        validateCommand(command);
        String encodedPassword = passwordEncoder.encode(command.password());
        Account account = new Account(
                Id.fast(),
                command.email().split("@")[0],
                command.email(),
                encodedPassword,
                command.displayName(),
                command.avatar(),
                command.dateOfBirth(),
                command.gender(),
                command.locale(),
                command.provider(),
                command.verifiedAt(),
                command.roles(),
                command.status()
        );

        accountRepository.saveAggregate(account);

        return account.getId();
    }

    private void validateCommand(CreateUserCommand command) {
        if (command.email() == null || command.email().isBlank()) {
            throw new BadRequestException("Email cannot be null or empty");
        }
        if (command.password() == null || command.password().length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters long");
        }
        if (command.displayName() != null && command.displayName().length() > 50) {
            throw new BadRequestException("Display name cannot exceed 50 characters");
        }

        if (command.dateOfBirth() != null && command.dateOfBirth().isAfter(LocalDate.now())) {
            throw new BadRequestException("Date of birth cannot be in the future");
        }
        if (command.dateOfBirth() != null && LocalDate.now().minusYears(13).isBefore(command.dateOfBirth())) {
            throw new BadRequestException("User must be at least 13 years old");
        }
    }
}
