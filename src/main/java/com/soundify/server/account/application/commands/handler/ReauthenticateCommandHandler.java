package com.soundify.server.account.application.commands.handler;

import com.soundify.server.account.application.commands.ReAuthenticateCommand;
import com.soundify.server.account.application.dto.TokenResponse;
import com.soundify.server.account.domain.models.Account;
import com.soundify.server.account.domain.models.AccountDomainRepository;
import com.soundify.server.account.domain.models.AccountStatus;
import com.soundify.server.account.infrastructure.security.*;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.mediator.Handler;
import com.soundify.server.shared.mediator.RequestHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.temporal.ChronoUnit;

@Handler
@RequiredArgsConstructor
@Log4j2
public class ReauthenticateCommandHandler implements RequestHandler<ReAuthenticateCommand, TokenResponse> {
    private final AccountDomainRepository accountRepository;
    private final JwtProvider tokenProvider;
    private final BlackListProvider blackListProvider;
    private final JwtDecoder jwtDecoder;
    @Value("${application.security.jwt.access-token-age}")
    private int accessTokenAge;
    @Value("${application.security.jwt.access-token-age-unit}")
    private ChronoUnit accessTokenUnit;

    @Override
    public TokenResponse handle(ReAuthenticateCommand request) {
        log.info("Reauthenticate with refresh token {}", request.refreshToken());
        Jwt jwt = jwtDecoder.decode(request.refreshToken());
        RefreshPrincipal refreshToken = tokenProvider.extractRefreshPrincipal(jwt);
        log.info("Reauthenticate with refresh token {}", refreshToken.id());
        log.info("Reauthenticate with refresh token device {}", refreshToken.deviceId());
        if (blackListProvider.containsRefreshTokenId(refreshToken.id())){
            log.info("Refresh token {} is blacklisted", refreshToken.id());
            throw new DisabledException("Refresh token is blacklisted");
        }

        if (blackListProvider.containsDeviceId(refreshToken.deviceId())){
            log.info("Refresh token {} is blacklisted", refreshToken.id());
            throw new DisabledException("Account is logout");
        }


        Account account = accountRepository.findAggregate(refreshToken.accountId());
        if (account.getStatus() != AccountStatus.ACTIVE){
            log.info("Account {} is not active", account.getId());
            throw new DisabledException("Account is disabled");
        }
        if (!account.isValidDevice(refreshToken.deviceId())){
            log.info("Invalid device {}", refreshToken.deviceId());
            throw new BadCredentialsException("Invalid device");
        }

        AccessToken accessToken = AccessToken.builder()
                .jti(Id.fast())
                .dob(account.getDateOfBirth())
                .locale(account.getLocale())
                .dev(refreshToken.deviceId())
                .sub(account.getId())
                .age(accessTokenAge)
                .unit(accessTokenUnit)
                .rid(refreshToken.id())
                .authorities(account.authorities())
                .build();

        Jwt accessTokenJwt = tokenProvider.generate(accessToken);


        return new TokenResponse(accessTokenJwt.getTokenValue(), AccessToken.TYPE);
    }
}
