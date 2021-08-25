package com.bwee.webit.auth;

import com.bwee.webit.model.WebitUser;
import com.bwee.webit.service.IdGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
class AuthTokenGeneratorTest {

    private AuthTokenGenerator tokenGenerator;
    private Clock clock;
    private WebitUser user;
    private IdGenerator<String> idGenerator;

    @BeforeEach
    public void before() {
        clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        user = new WebitUser().setId("123").setName("John").setRoles(List.of("ADMIN", "CASHIER"));

        idGenerator = mock(IdGenerator.class);
        when(idGenerator.generateId(any())).thenReturn("AAA" , "BBB", "CCC");

        tokenGenerator = new AuthTokenGenerator(clock, idGenerator, "testkey");
    }

    @Test
    public void testGenerateToken_shouldReturnToken() {
        final String token = tokenGenerator.generateToken(user);
        assertThat(token).isNotEmpty();
    }

    @Test
    public void testGenerateToken_shouldContainUserDetails() {
        final String token = tokenGenerator.generateToken(user);

        final Claims claims = Jwts.parser().setSigningKey("testKey").parseClaimsJws(token).getBody();

        assertThat(claims.getSubject()).isEqualTo("123");
        assertThat(claims).extractingByKey("name").isEqualTo("John");
        assertThat(claims).extractingByKey("roles")
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsExactlyInAnyOrder("ADMIN", "CASHIER");
    }

    @Test
    public void testGenerateToken_shouldContainTokenId() {
        final String token = tokenGenerator.generateToken(user);
        final Claims claims = Jwts.parser().setSigningKey("testKey").parseClaimsJws(token).getBody();

        assertThat(claims.getId()).isEqualTo("AAA");
    }

    @Test
    public void testGenerateToken_shouldContainIssueDate() {
        final String token = tokenGenerator.generateToken(user);
        log.info("TOKEN: {}", token);
        final Claims claims = Jwts.parser().setSigningKey("testKey").parseClaimsJws(token).getBody();

        assertThat(claims.getIssuedAt().toInstant()).isEqualTo(clock.instant().truncatedTo(ChronoUnit.SECONDS));
    }
}