package com.bwee.webit.auth;

import com.bwee.webit.auth.AuthTokenVerifier;
import com.bwee.webit.auth.AuthUser;
import com.bwee.webit.auth.ClaimNames;
import com.bwee.webit.exception.InvalidTokenException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.Clock;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        final AuthUser user = verifier.verifyToken(validToken);
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo("123");
        assertThat(user.getName()).isEqualTo("John");
        assertThat(user.getRoles()).containsExactlyInAnyOrder("ADMIN", "SUPV");
    }

    @Test
    public void testVerifyInvalidSignatureToken_shouldThrowException() {
        assertThatThrownBy(() -> verifier.verifyToken(invalidSignatureToken)).isInstanceOf(InvalidTokenException.class);
    }

    @Test
    public void testVerifyMalformedToken_shouldThrowException() {
        assertThatThrownBy(() -> verifier.verifyToken("MALFORMED_TOKEN")).isInstanceOf(InvalidTokenException.class);
    }

    @Test
    public void testVerifyInvalidToken_shouldThrowException() {
        assertThatThrownBy(() -> verifier.verifyToken("AN.INVALID.TOKEN")).isInstanceOf(InvalidTokenException.class);
    }
}