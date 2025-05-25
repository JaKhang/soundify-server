package com.soundify.server.account.http;

import com.soundify.server.account.application.commands.LogoutCommand;
import com.soundify.server.account.application.commands.LogoutDeviceCommand;
import com.soundify.server.account.application.commands.RequestVerifyCodeCommand;
import com.soundify.server.account.application.dto.DeviceResponse;
import com.soundify.server.account.application.queries.GetDeviceQuery;
import com.soundify.server.account.http.request.VerifyRequest;
import com.soundify.server.account.infrastructure.security.UserPrincipal;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.mediator.Mediator;
import com.soundify.server.shared.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountApi {
    private final Mediator gateway;


    @GetMapping("")
    public String test() {
        return "account";
    }

    @GetMapping("/principal")
    public Principal test2(@AuthenticationPrincipal Principal principal) {
        return principal;
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
    public void logout(@AuthenticationPrincipal UserPrincipal principal){
        gateway.send(new LogoutCommand(principal));
    }

    @PostMapping("/devices/{deviceId}/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logoutDevice(@PathVariable Id deviceId, @AuthenticationPrincipal UserPrincipal principal){
        gateway.send(new LogoutDeviceCommand(principal, deviceId));
    }








}
