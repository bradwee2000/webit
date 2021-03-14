package com.bwee.webit.server.controller;

import com.bwee.webit.auth.AuthenticationService;
import com.bwee.webit.server.model.LoginRes;
import com.bwee.webit.service.WebitUserService;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotEmpty;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private WebitUserService webitUserService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity login(@RequestBody final LoginReq loginReq) {
        final String token = authenticationService.login(loginReq.getUsername(), loginReq.getPassword());
        return ResponseEntity.ok(new LoginRes().setAccessToken(token));
    }

    @Data
    @Accessors(chain = true)
    public static class LoginReq {
        @NotEmpty
        private String username;

        @NotEmpty
        private String password;
    }
}
