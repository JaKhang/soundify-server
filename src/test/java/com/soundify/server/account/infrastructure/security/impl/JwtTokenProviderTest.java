package com.soundify.server.account.infrastructure.security.impl;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.soundify.server.account.infrastructure.security.AccessToken;
import com.soundify.server.account.infrastructure.security.KeyPairLoader;
import com.soundify.server.account.infrastructure.security.RefreshToken;
import com.soundify.server.account.infrastructure.security.UserPrincipal;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.account.application.exceptions.AuthenticationException;
import com.soundify.server.shared.security.Principal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    JwtEncoder jwtEncoder;
    JwtDecoder jwtDecoder;
    JwtTokenProvider tokenProvider;
    Id accessTokenId = Id.from("01JRSVHH7B2J33HZ0G8Q7EBR27");
    Id refreshTokenId = Id.from("01JRSVJ9X42SWNNCYADHY15YB1");
    Id deviceId = Id.from("01JRSVJKTV2EQ7Z9WDR0R999XW");
    Id userId = Id.from("01JRSVKEGJQFN40M3DTHNBF46H");

    @BeforeEach
    void setUp() {
        KeyPairLoader keyPairLoader = new KeyPairLoader("key/jwt-public.pem", "key/jwt-private.pem");
        KeyPair keyPair = keyPairLoader.getKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        var immutableJWKSet = new ImmutableJWKSet<>(jwkSet);

        jwtEncoder = new NimbusJwtEncoder(immutableJWKSet);
        tokenProvider = new JwtTokenProvider(jwtEncoder);
        jwtDecoder = NimbusJwtDecoder.withPublicKey((RSAPublicKey) keyPair.getPublic()).build();
    }

    @Test
    public void givenAccessToken_whenGenerate_thenJwtIsEncodedWithCorrectClaims() {
        // Given
        AccessToken accessToken = new AccessToken(
                accessTokenId,
                userId,
                refreshTokenId,
                10,
                Locale.ENGLISH,
                SECONDS,
                LocalDate.now(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // When
        Jwt jwt = tokenProvider.generate(accessToken);

        // Then
        assertNotNull(jwt);
        assertNotNull(jwt.getTokenValue());

        // Decode and verify claims
        Jwt decodedJwt = jwtDecoder.decode(jwt.getTokenValue());
        assertEquals(userId.toString(), decodedJwt.getSubject());
        assertEquals(refreshTokenId.toString(), decodedJwt.getClaim("rid"));
        assertEquals(accessTokenId.toString(), decodedJwt.getId());
        assertEquals("en", decodedJwt.getClaim("locale"));
        assertNotNull(decodedJwt.getClaim("authorities"));

        // Verify expiration time

        Instant expectedExpiration = Instant.now().plus(10, SECONDS);
        Instant actualExpiration = decodedJwt.getExpiresAt();

        // Allow 5 seconds tolerance for test execution time
        assertTrue(Math.abs(expectedExpiration.getEpochSecond() - actualExpiration.getEpochSecond()) <= 5);
    }

    @Test
    public void givenRefreshToken_whenGenerate_thenJwtIsEncodedWithCorrectClaims() {
        // Given
        RefreshToken refreshToken = RefreshToken.builder()
                .jti(refreshTokenId)
                .sub(userId)
                .dev(deviceId)
                .age(30)
                .unit(DAYS)
                .build();

        // When
        Jwt jwt = tokenProvider.generate(refreshToken);

        // Then
        assertNotNull(jwt);
        assertNotNull(jwt.getTokenValue());

        // Decode and verify claims
        Jwt decodedJwt = jwtDecoder.decode(jwt.getTokenValue());
        assertEquals(userId.toString(), decodedJwt.getSubject());
        assertEquals(deviceId.toString(), decodedJwt.getClaim("dev"));
        assertEquals(refreshTokenId.toString(), decodedJwt.getId());

        // Verify expiration time
        Instant expectedExpiration = Instant.now().plus(30, DAYS);
        Instant actualExpiration = decodedJwt.getExpiresAt();
        // Allow 5 seconds tolerance for test execution time
        assertTrue(Math.abs(expectedExpiration.getEpochSecond() - actualExpiration.getEpochSecond()) <= 5);

    }


    @Test
    public void givenAccessTokenWithMultipleAuthorities_whenGenerate_thenJwtContainsAllAuthorities() {
        // Given
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("PERMISSION_READ"),
                new SimpleGrantedAuthority("PERMISSION_WRITE")
        );

        AccessToken accessToken = new AccessToken(
                accessTokenId,
                userId,
                refreshTokenId,
                10,
                Locale.ENGLISH,
                SECONDS,
                LocalDate.now(),
                authorities
        );

        // When
        Jwt jwt = tokenProvider.generate(accessToken);

        // Then
        assertNotNull(jwt);

        // Decode and verify authorities
        Jwt decodedJwt = jwtDecoder.decode(jwt.getTokenValue());
        List<?> decodedAuthorities = decodedJwt.getClaim("authorities");
        assertNotNull(decodedAuthorities);
        assertEquals(4, decodedAuthorities.size());
        assertTrue(decodedAuthorities.contains("ROLE_USER"));
        assertTrue(decodedAuthorities.contains("ROLE_ADMIN"));
        assertTrue(decodedAuthorities.contains("PERMISSION_READ"));
        assertTrue(decodedAuthorities.contains("PERMISSION_WRITE"));
    }

    @Test
    public void givenValidJwt_whenExtractPrincipal_thenReturnCorrectPrincipal() {
        // Given
        AccessToken accessToken = new AccessToken(
                accessTokenId,
                userId,
                refreshTokenId,
                10,
                Locale.ENGLISH,
                SECONDS,
                LocalDate.of(2000, 1, 1),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        Jwt jwt = tokenProvider.generate(accessToken);

        // When
        UserPrincipal principal = tokenProvider.extractPrincipal(jwt);

        // Then
        assertNotNull(principal);
        assertEquals(userId, principal.id());
        assertEquals(1, principal.authorities().size());
        assertTrue(principal.authorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertEquals(LocalDate.of(2000, 1, 1), principal.dateOfBirth());
        assertEquals(Locale.ENGLISH, principal.locale());
    }

    @Test
    public void givenRefreshTokenJwt_whenExtractPrincipal_thenThrowException(){
        // Given
        RefreshToken refreshToken = RefreshToken.builder()
                .jti(refreshTokenId)
                .sub(userId)
                .dev(deviceId)
                .age(30)
                .unit(DAYS)
                .build();
        Jwt jwt = tokenProvider.generate(refreshToken);

        // When

        assertThrows(AuthenticationException.class, () -> tokenProvider.extractPrincipal(jwt));
    }

    @Test
    public void givenAccessTokenWithDifferentLocale_whenGenerate_thenJwtContainsCorrectLocale() {
        // Given
        AccessToken accessToken = new AccessToken(
                accessTokenId,
                userId,
                refreshTokenId,
                10,
                Locale.FRENCH,
                SECONDS,
                LocalDate.now(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // When
        Jwt jwt = tokenProvider.generate(accessToken);

        // Then
        Jwt decodedJwt = jwtDecoder.decode(jwt.getTokenValue());
        assertEquals("fr", decodedJwt.getClaim("locale"));

        // Extract principal and verify locale
        Principal principal = tokenProvider.extractPrincipal(jwt);
        assertEquals(Locale.FRENCH, principal.locale());
    }

    @Test
    public void givenAccessTokenWithDifferentExpirationTime_whenGenerate_thenJwtHasCorrectExpiration() {
        // Given - token with 1 day expiration
        AccessToken accessToken = new AccessToken(
                accessTokenId,
                userId,
                refreshTokenId,
                1,
                Locale.ENGLISH,
                DAYS,
                LocalDate.now(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // When
        Jwt jwt = tokenProvider.generate(accessToken);

        // Then
        Jwt decodedJwt = jwtDecoder.decode(jwt.getTokenValue());
        Instant expectedExpiration = Instant.now().plus(1, DAYS);
        Instant actualExpiration = decodedJwt.getExpiresAt();
        // Allow 5 seconds tolerance for test execution time
        assertTrue(Math.abs(expectedExpiration.getEpochSecond() - actualExpiration.getEpochSecond()) <= 5);

    }
}
