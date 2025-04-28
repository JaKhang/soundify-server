package com.soundify.server.account.http;

import com.soundify.server.account.application.commands.AuthenticateCommand;
import com.soundify.server.account.application.commands.ReAuthenticateCommand;
import com.soundify.server.account.application.dto.response.AuthenticationResponse;
import com.soundify.server.account.application.dto.response.TokenResponse;
import com.soundify.server.account.application.dto.request.LocalAuthenticateRequest;
import com.soundify.server.account.application.dto.request.ReauthenticateRequest;
import com.soundify.server.shared.mediator.Mediator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua_parser.Client;
import ua_parser.Parser;

import static com.soundify.server.account.infrastructure.utils.HttpUtils.extractIp;

@RestController 
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final Mediator gateway;
    private final Parser parser;
    private static final String DEV_PROFILE = "dev";
    @Value("${application.profile}")
    private String profile;

    @PostMapping("/authenticate")
    public ResponseEntity<TokenResponse> authenticate(
            @RequestBody @Valid LocalAuthenticateRequest body,
            @RequestHeader("user-agent") String userAgent,
            HttpServletRequest request
    ) {
        Client client = parser.parse(userAgent);
        String ip = extractIp(request);

        AuthenticationResponse response = gateway.send(new AuthenticateCommand(
                body.usernameOrEmail(),
                body.password(),
                client.os.family,
                ip,
                client.userAgent.family
        ));



        ResponseCookie cookie = ResponseCookie.from("refreshToken", response.refreshToken().token())
                .httpOnly(true)
                .secure(!profile.equals(DEV_PROFILE))
                .path("/")
                .maxAge(response.age())
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(response.accessToken());
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
