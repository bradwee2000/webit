package com.bwee.webit.auth;

import com.bwee.webit.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
public class AuthTokenVerifier {

    private final String secretKey;

    public AuthTokenVerifier(final String secretKey) {
        this.secretKey = secretKey;
    }

    public Optional<AuthUser> verifyToken(final String token) {
        if (StringUtils.isEmpty(token)) {
            return Optional.empty();
        }

        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            return Optional.of(AuthUser.builder()
                    .token(token)
                    .userId(claims.getSubject())
                    .name(claims.get(ClaimNames.NAME, String.class))
                    .roles(claims.get(ClaimNames.ROLES, List.class))
                    .build());
        } catch (final JwtException e) {
            log.info("Failed to verify token. reason={}, token={}", e.getMessage(), token);
            return Optional.empty();
        }
    }
}
