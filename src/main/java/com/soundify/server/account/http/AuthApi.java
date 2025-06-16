package com.soundify.server.account.http;

import com.soundify.server.account.application.commands.*;
import com.soundify.server.account.http.request.LocalAuthenticateRequest;
import com.soundify.server.account.application.dto.AuthenticationResponse;
import com.soundify.server.account.application.dto.TokenResponse;
import com.soundify.server.account.domain.models.AccountStatus;
import com.soundify.server.account.domain.models.Provider;
import com.soundify.server.account.domain.models.Role;
import com.soundify.server.account.http.request.RegisterRequest;
import com.soundify.server.account.http.request.VerifyRequest;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.BadRequestException;
import com.soundify.server.shared.mediator.Mediator;
import com.soundify.server.shared.security.Principal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ua_parser.Client;
import ua_parser.Parser;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.soundify.server.account.infrastructure.utils.HttpUtils.extractIp;

@Log4j2
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final Mediator gateway;
    private final Parser parser;
    private static final String DEV_PROFILE = "dev";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    @Value("${spring.profiles.active}")
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



        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, response.refreshToken().token())
                .httpOnly(true)
                .secure(!profile.equals(DEV_PROFILE))
                .path("/")
                .sameSite(Cookie.SameSite.LAX.attributeValue()) // Quan trọng nếu frontend và backend ở domain khác nhau
                .maxAge(response.age())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response.accessToken());
    }

    @GetMapping("/access-token")
    public TokenResponse reauthenticate(
            @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken,
            @RequestHeader("user-agent") String userAgent,
            HttpServletRequest request) {
        if (refreshToken == null) {
            throw new BadRequestException("refreshToken cookie is required");
        }
        Client client = parser.parse(userAgent);
        String ip = extractIp(request);
        log.debug("Reauthenticate with refresh token {}", refreshToken);
        return gateway.send(new ReAuthenticateCommand(refreshToken, ip, client.os.family, client.userAgent.family));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Id register(
            @RequestBody @Valid RegisterRequest request,
            HttpServletRequest httpRequest
    ) {
        Locale locale = httpRequest.getLocale();

        return gateway.send(new CreateUserCommand(
                request.email(),
                request.password(), // Password will be handled separately (not in RegisterRequest)
                request.displayName(),
                List.of(), // Avatar list is optional
                request.dateOfBirth(),
                request.gender(),
                locale, // Use the extracted locale
                Provider.LOCAL_PROVIDER, // Provider is optional
                null, // VerifiedAt is optional
                Set.of(Role.USER), // Roles are optional
                AccountStatus.ACTIVE  // Status is optional
        ));
    }

    @PostMapping("/verify")
    public void verify(@RequestBody VerifyRequest verifyRequest){
        gateway.send(new VerifyCommand(verifyRequest.token(), verifyRequest.email()));
    }

    @PutMapping("/verify")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requestVerify(@RequestParam String email){
        gateway.send(new RequestVerifyCodeCommand(email));
    }

    @PutMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requestResetPassword(@RequestParam String email){
        gateway.send(new RequestVerifyCodeCommand(email));
    }


    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@RequestParam String email){
        gateway.send(new RequestVerifyCodeCommand(email));
    }


}
