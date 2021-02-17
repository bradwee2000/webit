package com.bwee.webit.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public String getLoginUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
