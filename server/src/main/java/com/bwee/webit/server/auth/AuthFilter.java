package com.bwee.webit.server.auth;

import com.bwee.webit.server.exception.UnauthorizedAccessException;
import org.springframework.beans.factory.support.SecurityContextProvider;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;

@Order(1)
@Component
public class AuthFilter implements Filter {
    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;

        final String token = req.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(token)) {
            throw new UnauthorizedAccessException(req);
        }

        final Authentication auth = new UsernamePasswordAuthenticationToken(token, token, Collections.emptyList());
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
