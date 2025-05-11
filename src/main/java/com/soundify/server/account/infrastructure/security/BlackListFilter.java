package com.soundify.server.account.infrastructure.security;

import com.soundify.server.account.application.exceptions.AuthenticationException;
import com.soundify.server.shared.exceptions.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class BlackListFilter extends OncePerRequestFilter {

    private final BackListProvider backListProvider;


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            filterChain.doFilter(request, response);
            return;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null) {
            if (principal instanceof UserPrincipal userPrincipal) {
                if (backListProvider.containsRefreshTokenId(userPrincipal.refreshTokenId())) {
                    throw new AuthorizationDeniedException("Access denied");
                }
            }
        }


        filterChain.doFilter(request, response);
    }
}
