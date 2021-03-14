package com.bwee.webit.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AuthTokenVerifierTest {

    private final Instant now = Clock.systemDefaultZone().instant();
    private final String secretKey = "aSecretTestKey";
    private final String validToken = Jwts.builder()
            .setSubject("123")
            .claim(ClaimNames.NAME, "John")
            .claim(ClaimNames.ROLES, List.of("ADMIN", "SUPV"))
            .setIssuedAt(Date.from(now))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();

    private final String invalidSignatureToken = Jwts.builder()
            .setSubject("123")
            .signWith(SignatureAlgorithm.HS256, "wrongKey")
            .compact();

    private AuthTokenVerifier verifier;

    @BeforeEach
    public void before() {
        verifier = new AuthTokenVerifier(secretKey);
    }

    @Test
    public void testVerifyToken_shouldReturnAuthUser() {
        final Optional<AuthUser> user = verifier.verifyToken(validToken);
        assertThat(user).isNotEmpty();
        assertThat(user.get().getUserId()).isEqualTo("123");
        assertThat(user.get().getName()).isEqualTo("John");
        assertThat(user.get().getRoles()).containsExactlyInAnyOrder("ADMIN", "SUPV");
    }

    @Test
    public void testVerifyInvalidSignatureToken_shouldReturnEmpty() {
        assertThat(verifier.verifyToken(invalidSignatureToken)).isEmpty();
    }

    @Test
    public void testVerifyMalformedToken_shouldReturnEmpty() {
        assertThat(verifier.verifyToken("MALFORMED_TOKEN")).isEmpty();
    }

    @Test
    public void testVerifyInvalidToken_shouldReturnEmpty() {
        assertThat(verifier.verifyToken("AN.INVALID.TOKEN")).isEmpty();
    }
}