package com.bwee.webit.auth;

import com.bwee.webit.service.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class AuthConfiguration {

    @Value("${auth.jwt.signing.key:testkey}")
    private String jwtSigningKey;

    @Bean
    public AuthTokenGenerator authTokenGenerator(final Clock clock, final RandomIdGenerator idGenerator) {
        return new AuthTokenGenerator(clock, idGenerator, jwtSigningKey);
    }

    @Bean
    public AuthTokenVerifier authTokenVerifier() {
        return new AuthTokenVerifier(jwtSigningKey);
    }
}
