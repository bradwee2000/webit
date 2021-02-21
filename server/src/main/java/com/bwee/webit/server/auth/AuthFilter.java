package com.bwee.webit.server.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
@Order(1)
@Component
public class AuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        final String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Accessing {} {} using {}", req.getMethod(), req.getRequestURI(), token);

        if (!StringUtils.isEmpty(token)) {
            final Authentication auth = new UsernamePasswordAuthenticationToken(token, token, Collections.emptyList());
            final SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(auth);
        }
        filterChain.doFilter(req, res);
    }
}
