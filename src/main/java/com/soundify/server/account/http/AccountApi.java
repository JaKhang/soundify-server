package com.soundify.server.account.http;

import com.soundify.server.account.application.commands.LogoutCommand;
import com.soundify.server.account.application.commands.LogoutDeviceCommand;
import com.soundify.server.account.application.dto.DeviceResponse;
import com.soundify.server.account.application.dto.PrincipalResponse;
import com.soundify.server.account.application.queries.GetDeviceQuery;
import com.soundify.server.account.application.queries.GetPrincipalQuery;
import com.soundify.server.account.infrastructure.security.UserPrincipal;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.mediator.Mediator;
import com.soundify.server.shared.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Set;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountApi {
    private final Mediator gateway;

    @Value("${application.security.cookie.same-site}")
    private String sameSite;
    @Value("${application.security.cookie.secure}")
    private boolean secure;
    @Value("${application.security.cookie.http-only}")
    private boolean httpOnly;
    @GetMapping("")
    public String test() {
        return "account";
    }

    @GetMapping("/principal")
    public PrincipalResponse getPrincipal(@AuthenticationPrincipal Principal principal) {
        return gateway.send(new GetPrincipalQuery(principal.id()));
    }

    @GetMapping("/admin")
    @PostAuthorize("hasRole('ADMIN')")
    public Authentication admin(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/devices")
    public Set<DeviceResponse> getDevices(@AuthenticationPrincipal UserPrincipal principal) {
        return gateway.send(new GetDeviceQuery(principal.id(), principal.deviceId()));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserPrincipal principal) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .path("/")
                .maxAge(Duration.ZERO)
                .sameSite(sameSite)
                .secure(secure)
                .httpOnly(httpOnly)
                .build();

        gateway.send(new LogoutCommand(principal));

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/devices/{deviceId}/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logoutDevice(@PathVariable Id deviceId, @AuthenticationPrincipal UserPrincipal principal){
        gateway.send(new LogoutDeviceCommand(principal, deviceId));

    }








}
