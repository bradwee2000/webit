package com.bwee.webit.auth;

import com.bwee.webit.exception.InvalidCredentialsException;
import com.bwee.webit.model.WebitUser;
import com.bwee.webit.service.WebitUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationService {

    @Autowired
    private WebitUserService userService;

    @Autowired
    private AuthTokenGenerator tokenGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String getLoginUserId() {
        final AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authUser.getUserId();
    }

    public String getAuthToken() {
        final AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authUser.getToken();
    }

    public AuthUser.LoginRes login(final String username, final String password) {
        final WebitUser user = userService.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException());

        final boolean isValid = passwordEncoder.matches(password, user.getPassword());

        if (!isValid) {
            throw new InvalidCredentialsException();
        }

        final String token = tokenGenerator.generateToken(user);

        return new AuthUser.LoginRes().setToken(token).setName(user.getName());
    }
}
