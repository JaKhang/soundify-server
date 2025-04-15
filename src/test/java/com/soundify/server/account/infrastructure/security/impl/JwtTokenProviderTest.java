package com.soundify.server.account.infrastructure.security.impl;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.soundify.server.account.infrastructure.security.AccessToken;
import com.soundify.server.account.infrastructure.security.KeyPairLoader;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.security.Principal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.SECONDS;

class JwtTokenProviderTest {

    JwtEncoder jwtEncoder;
    JwtDecoder jwtDecoder ;
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
    public void givenAccessToken_whenGenerate_thenJwtIsEncoded() {
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

        Jwt jwt = tokenProvider.generate(accessToken);


        Principal principal = tokenProvider.extractPrincipal(jwt);
        System.out.println(principal);
        System.out.println(jwt.getTokenValue());

    }

    @Test
    void test(){
        Jwt jwt = jwtDecoder.decode("eyJraWQiOiIzMDVlOTcwZi1iNjY3LTQzODAtOThkNi01YjEyNjBhNmZkNzUiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIwMUpSU1ZLRUdKUUZONDBNM0RUSE5CRjQ2SCIsImRvYiI6IjIwMjUtMDQtMTQiLCJleHAiOjE3NDQ2MjY2MzMsImxvY2FsZSI6ImVuIiwicmlkIjoiMDFKUlNWSjlYNDJTV05OQ1lBREhZMTVZQjEiLCJpYXQiOjE3NDQ2MjY2MjMsImp0aSI6IjAxSlJTVkhIN0IySjMzSFowRzhRN0VCUjI3IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl19.PizBB4_vomi-MTD3WtLFVjLKQlWWwNvO0l2CrDwSJztWdWa8KG8tXAvHNhpnjOslsvHKdGqAtJjoL_2v_ara19t0sW3cQhE4I5uYm2NnHIOfCHwGsgJOvtLp1i8de6iyECRQCaduVAaUvDFshGshO1d5UNNvAS1xhgpFtwKhVrLydb_NTuZEoM53sz88iER2esTjuglDyVAATfrsuNTb6dj5cRDHa2Uq7AFa6eYFkGKNFgUxjRqSrsj3ZdMFTqX5dQKVMVB_bTKWhNBw4BoCyO82d532EzZV6n0GD389lIg2XMmtMPOncag5lk3zMzle7C5fGmzjfqzam77UP6ugtA");
        Principal principal = tokenProvider.extractPrincipal(jwt);
        System.out.println(principal);
    }


}