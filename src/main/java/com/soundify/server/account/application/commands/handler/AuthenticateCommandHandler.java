package com.soundify.server.account.application.commands.handler;

import com.soundify.server.account.application.commands.AuthenticateCommand;
import com.soundify.server.account.application.dto.TokenResponse;
import com.soundify.server.account.domain.models.Account;
import com.soundify.server.account.domain.models.AccountDomainRepository;
import com.soundify.server.account.domain.models.AccountStatus;
import com.soundify.server.account.domain.models.Device;
import com.soundify.server.account.infrastructure.security.RefreshToken;
import com.soundify.server.account.infrastructure.security.TokenProvider;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.AuthenticationException;
import com.soundify.server.shared.exceptions.ErrorCode;
import com.soundify.server.shared.mediator.Handler;
import com.soundify.server.shared.mediator.RequestHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.temporal.ChronoUnit;

@Slf4j
@Handler
@RequiredArgsConstructor
public class AuthenticateCommandHandler implements RequestHandler<AuthenticateCommand, TokenResponse> {
    private final AccountDomainRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;


    @Value("${application.security.jwt.refresh-token-age}")
    private int refreshTokenAge;
    @Value("${application.security.jwt.refresh-token-age-unit}")
    private ChronoUnit refreshTokenUnit;

    @Override
    @Transactional
    public TokenResponse handle(AuthenticateCommand request) {
        log.info("Authenticate with credentials {}" ,request.usernameOrEmail());
        Account account = accountRepository.findAggregateByUsernameOrEmail(request.usernameOrEmail());
        if (account.getStatus() != AccountStatus.ACTIVE)
            throw new AuthenticationException(ErrorCode.ACCOUNT_NOT_ACTIVE);

        if (!passwordEncoder.matches(request.password(), account.getPassword()))
            throw new AuthenticationException(ErrorCode.BAD_CREDENTIALS);

        Device device = account.registerDevice(request.os(), request.ip(), request.platform(), refreshTokenAge, refreshTokenUnit);
        accountRepository.saveAggregate(account);
        RefreshToken refreshToken = RefreshToken.builder()
                .dev(device.getId())
                .jti(Id.fast())
                .sub(account.getId())
                .age(refreshTokenAge)
                .unit(refreshTokenUnit)
                .build();
        Jwt jwt = tokenProvider.generate(refreshToken);
        return new TokenResponse(jwt.getTokenValue(), RefreshToken.TYPE);
    }
}
