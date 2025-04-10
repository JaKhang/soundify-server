package com.soundify.server.account.http;

import com.soundify.server.account.application.commands.AuthenticateCommand;
import com.soundify.server.account.application.commands.ReAuthenticateCommand;
import com.soundify.server.account.application.dto.TokenResponse;
import com.soundify.server.account.application.dto.request.LocalAuthenticateRequest;
import com.soundify.server.account.application.dto.request.ReauthenticateRequest;
import com.soundify.server.shared.mediator.Mediator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua_parser.Client;
import ua_parser.Parser;

import static com.soundify.server.account.infrastructure.utils.HttpUtils.extractIp;

@RestController
@RequestMapping("/api/v1/auth")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthApi {

    Mediator gateway;
    Parser parser;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/authenticate")
    public TokenResponse authenticate(
            @RequestBody @Valid LocalAuthenticateRequest body,
            @RequestHeader("user-agent") String userAgent,
            HttpServletRequest request
    ) {
        Client client = parser.parse(userAgent);
        String ip = extractIp(request);
        return gateway.send(new AuthenticateCommand(
                body.usernameOrEmail(),
                body.password(),
                client.os.family,
                ip,
                client.userAgent.family
        ));
    }

    @PostMapping("/reauthenticate")
    public TokenResponse reauthenticate(
            @RequestBody @Valid ReauthenticateRequest body,
            @RequestHeader("user-agent") String userAgent,
            HttpServletRequest request) {
        Client client = parser.parse(userAgent);
        String ip = extractIp(request);

        return gateway.send(new ReAuthenticateCommand(body.refreshToken(), ip, client.os.family, client.userAgent.family));
    }
}
