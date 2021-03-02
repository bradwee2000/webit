package com.bwee.webit.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationService {

    public String getLoginUserId() {
        final AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authUser.getUserId();
    }

    public String getAuthToken() {
        final AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authUser.getToken();
    }
}
