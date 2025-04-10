package com.soundify.server.account.infrastructure.security;

import com.soundify.server.shared.data.UserPrincipal;
import com.soundify.server.shared.exceptions.AuthenticationException;
import com.soundify.server.shared.exceptions.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class BlackListFilter extends OncePerRequestFilter {

    private final BackListProvider backListProvider;


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserPrincipal userPrincipal) {
            if (backListProvider.containsRefreshTokenId(userPrincipal.getRefreshTokenId())) {
                throw new AuthenticationException(ErrorCode.FORBIDDEN);
            }
        }

        filterChain.doFilter(request, response);
    }
}
