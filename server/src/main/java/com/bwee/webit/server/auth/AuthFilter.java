package com.bwee.webit.server.auth;

import com.bwee.webit.auth.AuthTokenVerifier;
import com.bwee.webit.auth.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private AuthTokenVerifier tokenVerifier;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        final String headerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Accessing {} {} using token={}", req.getMethod(), req.getRequestURI(), headerToken);

//        final Enumeration<String> enumeration = req.getHeaderNames();
//        while(enumeration.hasMoreElements()) {
//            final String key = enumeration.nextElement();
//            log.info(" -- HEADER: {}={}", key, req.getHeader(key));
//        }

        tokenVerifier.verifyToken(headerToken).ifPresent(user -> {
            final List<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());

            final Authentication auth = new UsernamePasswordAuthenticationToken(user, headerToken, grantedAuthorities);
            final SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(auth);
        });

        filterChain.doFilter(req, res);
    }
}
