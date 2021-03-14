package com.bwee.webit.auth;

import com.bwee.webit.service.IdGenerator;
import com.bwee.webit.model.WebitUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.sql.Date;
import java.time.Clock;

public class AuthTokenGenerator {

    private final Clock clock;
    private final String secretKey;
    private final IdGenerator idGenerator;

    public AuthTokenGenerator(final Clock clock,
                              final IdGenerator idGenerator,
                              final String secretKey) {
        this.clock = clock;
        this.idGenerator = idGenerator;
        this.secretKey = secretKey;
    }

    public String generateToken(final WebitUser user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .setId(idGenerator.generateId(null))
                .claim(ClaimNames.NAME, user.getName())
                .claim(ClaimNames.ROLES, user.getRoles())
                .setIssuedAt(Date.from(clock.instant()))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
