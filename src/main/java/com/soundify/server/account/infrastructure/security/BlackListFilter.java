package com.soundify.server.account.infrastructure.security;

import com.soundify.server.security.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class BlackListFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Lấy thông tin user từ SecurityContext
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserPrincipal userPrincipal) {
            // Kiểm tra nếu refreshTokenId có trong blacklist
            if (blacklistService.isBlacklisted(userPrincipal.getRefreshTokenId())) {
                throw new SecurityException("Token is blacklisted.");
            }
        }

        // Tiếp tục chuỗi filter
        filterChain.doFilter(request, response);
    }
}
