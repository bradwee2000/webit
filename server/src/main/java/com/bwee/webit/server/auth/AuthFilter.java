package com.bwee.webit.server.auth;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

@Slf4j
@Component
public class AuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        final String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Accessing {} {} using {}", req.getMethod(), req.getRequestURI(), token);

        final Enumeration<String> enumeration = req.getHeaderNames();
        while(enumeration.hasMoreElements()) {
            final String key = enumeration.nextElement();
            log.info(" -- HEADER: {}={}", key, req.getHeader(key));
        }

        final SecurityContext securityContext = SecurityContextHolder.getContext();

        if (StringUtils.equals(req.getParameter("token"), "deeznuts")) {
            final String paramToken = req.getParameter("token");
            final Authentication auth = new UsernamePasswordAuthenticationToken(paramToken, paramToken, Collections.emptyList());
            securityContext.setAuthentication(auth);
        } else if (!StringUtils.isEmpty(token)) {
            final Authentication auth = new UsernamePasswordAuthenticationToken(token, token, Collections.emptyList());
            securityContext.setAuthentication(auth);
        }
        filterChain.doFilter(req, res);
    }
}
