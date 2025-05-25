package com.soundify.server.account.application.commands.handler;

import com.soundify.server.account.application.commands.AuthenticateCommand;
import com.soundify.server.account.application.dto.AuthenticationResponse;
import com.soundify.server.account.application.dto.TokenResponse;
import com.soundify.server.account.domain.models.Account;
import com.soundify.server.account.domain.models.AccountDomainRepository;
import com.soundify.server.account.domain.models.AccountStatus;
import com.soundify.server.account.domain.models.Device;
import com.soundify.server.account.infrastructure.security.AccessToken;
import com.soundify.server.account.infrastructure.security.RefreshToken;
import com.soundify.server.account.infrastructure.security.JwtProvider;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.mediator.Handler;
import com.soundify.server.shared.mediator.RequestHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@Handler
@RequiredArgsConstructor
public class AuthenticateCommandHandler implements RequestHandler<AuthenticateCommand, AuthenticationResponse> {
    private final AccountDomainRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider tokenProvider;


    @Value("${application.security.jwt.refresh-token-age}")
    private int refreshTokenAge;
    @Value("${application.security.jwt.refresh-token-age-unit}")
    private ChronoUnit refreshTokenUnit;

    @Value("${application.security.jwt.access-token-age}")
    private int accessTokenAge;
    @Value("${application.security.jwt.access-token-age-unit}")
    private ChronoUnit accessTokenUnit;

    @Override
    @Transactional
    public AuthenticationResponse handle(AuthenticateCommand request) {
        log.info("Authenticate with credentials {}" ,request.usernameOrEmail());
        Account account = accountRepository.findAggregateByUsernameOrEmail(request.usernameOrEmail());
        if (account.getStatus() != AccountStatus.ACTIVE)
            throw new DisabledException("Account is disabled");

        if (!passwordEncoder.matches(request.password(), account.getPassword()))
            throw new BadCredentialsException("Invalid password");

        Device device = account.registerDevice(request.os(), request.ip(), request.platform(), refreshTokenAge, refreshTokenUnit);
        accountRepository.saveAggregate(account);
        RefreshToken refreshToken = RefreshToken.builder()
                .dev(device.getId())
                .jti(Id.fast())
                .sub(account.getId())
                .age(refreshTokenAge)
                .unit(refreshTokenUnit)
                .build();
        Jwt refreshTokenJwt = tokenProvider.generate(refreshToken);


        AccessToken accessToken = AccessToken.builder()
                .jti(Id.fast())
                .dob(account.getDateOfBirth())
                .locale(account.getLocale())
                .sub(account.getId())
                .age(accessTokenAge)
                .dev(device.getId())
                .unit(accessTokenUnit)
                .rid(refreshToken.getJti())
                .authorities(account.authorities())
                .build();
        Jwt accessTokenJwt = tokenProvider.generate(accessToken);

        return new AuthenticationResponse(new TokenResponse(accessTokenJwt.getTokenValue(), AccessToken.TYPE),new TokenResponse(refreshTokenJwt.getTokenValue(), RefreshToken.TYPE), Duration.of(refreshTokenAge, refreshTokenUnit));
    }
}
