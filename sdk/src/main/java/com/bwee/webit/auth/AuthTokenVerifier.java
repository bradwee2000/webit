package com.bwee.webit.auth;

import com.bwee.webit.exception.InvalidTokenException;
import io.jsonwebtoken.*;

import java.util.List;

public class AuthTokenVerifier {

    private final String secretKey;

    public AuthTokenVerifier(final String secretKey) {
        this.secretKey = secretKey;
    }

    public AuthUser verifyToken(final String token) {
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            return AuthUser.builder()
                    .token(token)
                    .userId(claims.getSubject())
                    .name(claims.get(ClaimNames.NAME, String.class))
                    .roles(claims.get(ClaimNames.ROLES, List.class))
                    .build();
        } catch (final ExpiredJwtException e) {
            throw InvalidTokenException.expired();
        } catch (final UnsupportedJwtException e) {
            throw InvalidTokenException.unsupported();
        } catch (MalformedJwtException e) {
            throw InvalidTokenException.malformed();
        } catch (SignatureException e) {
            throw InvalidTokenException.invalidSignature();
        }
    }
}
