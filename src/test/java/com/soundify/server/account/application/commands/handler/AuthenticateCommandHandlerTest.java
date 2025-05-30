package com.soundify.server.account.application.commands.handler;

import com.soundify.server.account.domain.models.AccountDomainRepository;
import com.soundify.server.account.infrastructure.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AuthenticateCommandHandlerTest {

    @Mock
    private AccountDomainRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider tokenProvider;

    @InjectMocks
    private AuthenticateCommandHandler handler;

    private final String TEST_USERNAME = "testuser";
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "password123";
    private final String TEST_OS = "Windows";
    private final String TEST_IP = "192.168.1.1";
    private final String TEST_PLATFORM = "Chrome";
    private final int REFRESH_TOKEN_AGE = 30;
    private final ChronoUnit REFRESH_TOKEN_UNIT = ChronoUnit.DAYS;
    private final String JWT_TOKEN_VALUE = "test.jwt.token";

    @BeforeEach
    void setUp() {
        // Set values for the handler's properties using reflection
        ReflectionTestUtils.setField(handler, "refreshTokenAge", REFRESH_TOKEN_AGE);
        ReflectionTestUtils.setField(handler, "refreshTokenUnit", REFRESH_TOKEN_UNIT);
    }



    private Jwt createMockJwt(String tokenValue) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", "user123");

        return new Jwt(
                tokenValue,
                Instant.now(),
                Instant.now().plusSeconds(3600),
                headers,
                claims
        );
    }
}